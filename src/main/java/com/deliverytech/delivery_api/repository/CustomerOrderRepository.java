package com.deliverytech.delivery_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.dto.TotalSalesByRestaurantDTO;
import com.deliverytech.delivery_api.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.model.CustomerOrder;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

  @Query(value = """
        SELECT DISTINCT co
        FROM CustomerOrder co
        JOIN FETCH co.customer c
        JOIN FETCH co.restaurant r
        LEFT JOIN FETCH co.orderItems i
        LEFT JOIN FETCH i.product p
        WHERE co.customer.id = :customerId
      """,
      countQuery = "SELECT count(co) FROM CustomerOrder co WHERE co.customer.id = :customerId")
  Page<CustomerOrder> findByCustomerIdWithItems(@Param("customerId") Long customerId, Pageable pageable);

  @Query("""
        SELECT DISTINCT co
        FROM CustomerOrder co
        JOIN FETCH co.customer c
        JOIN FETCH co.restaurant r
        LEFT JOIN FETCH co.orderItems i
        LEFT JOIN FETCH i.product p
        WHERE co.id = :orderId
      """)
  CustomerOrder findByIdWithItems(@Param("orderId") Long orderId);

  List<CustomerOrder> findByCustomerId(Long customerId);

  List<CustomerOrder> findByStatus(CustomerOrderStatus status);

  @Query("""
        SELECT co FROM CustomerOrder co
        WHERE co.restaurant.id = :restaurantId
      """)
  List<CustomerOrder> findByDateTime(
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  @Query("""
      select new com.deliverytech.delivery_api.dto.TotalSalesByRestaurantDTO(
              r.name,
              coalesce(sum(oi.subtotal), 0)
          )
          from CustomerOrder co
          join co.restaurant r
          join co.orderItems oi
          group by r.name
      """)
  List<TotalSalesByRestaurantDTO> totalSalesByRestaurant();

  @Query(value="""
              SELECT c.name AS customer, COUNT(co.id) AS total_orders
              FROM customer_orders co 
              JOIN customers c ON c.id = co.customer_id
              GROUP BY c.name
              ORDER BY total_orders DESC
      """, nativeQuery = true )
  List<Object[]> customerRanking();
}

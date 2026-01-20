package com.deliverytech.delivery_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.model.CustomerOrder;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
  List<CustomerOrder> findByCustomerId(Long customerId);

  List<CustomerOrder> findByStatus(CustomerOrderStatus status);

  @Query("""
        SELECT co FROM CustomerOrder co
        WHERE co.restaurant.id = :restaurantId
      """)
  List<CustomerOrder> findByDateTime(
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);
}

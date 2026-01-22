package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.model.Customer;
import com.deliverytech.delivery_api.model.CustomerOrder;
import com.deliverytech.delivery_api.model.OrderItem;
import com.deliverytech.delivery_api.model.Product;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.repository.CustomerOrderRepository;
import com.deliverytech.delivery_api.repository.CustomerRepository;
import com.deliverytech.delivery_api.repository.OrderItemRepository;
import com.deliverytech.delivery_api.repository.ProductRepository;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerOrderService {
  @Autowired
  private CustomerOrderRepository customerOrderRepository;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private RestaurantRepository restaurantRepository;
  @Autowired
  private OrderItemRepository orderItemRepository;
  @Autowired
  private ProductRepository productRepository;

  public CustomerOrderService(CustomerOrderRepository customerOrderRepository, CustomerRepository customerRepository,
      RestaurantRepository restaurantRepository, OrderItemRepository orderItemRepository,
      ProductRepository productRepository) {
    this.customerOrderRepository = customerOrderRepository;
    this.customerRepository = customerRepository;
    this.restaurantRepository = restaurantRepository;
    this.orderItemRepository = orderItemRepository;
    this.productRepository = productRepository;
  }

  public CustomerOrder createOrder(Long customerId, Long restaurantId) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

    CustomerOrder newOrder = new CustomerOrder();
    newOrder.setCustomer(customer);
    newOrder.setRestaurant(restaurant);
    // newOrder.setOrderNumber(null); - AVALIAR COMO SERÁ FEITO
    newOrder.setStatus(CustomerOrderStatus.PENDENTE);
    newOrder.setDateOrder(LocalDateTime.now());
    newOrder.setTotalValue(BigDecimal.ZERO);

    return customerOrderRepository.save(newOrder);
  }

  public CustomerOrder updateStatusOrder(Long orderId, CustomerOrderStatus status) {
    CustomerOrder existingOrder = customerOrderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

    existingOrder.setStatus(status);
    return customerOrderRepository.save(existingOrder);
  }

  public List<CustomerOrder> getOrdersByCustomerId(Long customerId) {
    return customerOrderRepository.findByCustomerId(customerId);
  }

  public List<CustomerOrder> getAllOrders() {
    return customerOrderRepository.findAll();
  }

  public OrderItem addOrderItem(Long orderId, Long productId, Integer quantity) {
    CustomerOrder order = customerOrderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

    OrderItem orderItem = new OrderItem();
    orderItem.setCustomerOrder(order);
    orderItem.setProduct(product);
    orderItem.setQuantity(quantity);
    orderItem.setUnitPrice(product.getPrice());
    BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    orderItem.setSubtotal(subtotal);
    orderItemRepository.save(orderItem);

    order.setTotalValue(order.getTotalValue().add(subtotal));
    customerOrderRepository.save(order);

    return orderItem;
  }

  public CustomerOrder getOrderById(Long id) {
    return customerOrderRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
  }
}
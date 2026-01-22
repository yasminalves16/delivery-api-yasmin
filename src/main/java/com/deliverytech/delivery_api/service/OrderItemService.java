package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.model.CustomerOrder;
import com.deliverytech.delivery_api.model.OrderItem;
import com.deliverytech.delivery_api.repository.CustomerOrderRepository;
import com.deliverytech.delivery_api.repository.OrderItemRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderItemService {
  private final OrderItemRepository orderItemRepository;

  public OrderItemService(CustomerOrderRepository customerOrderRepository,
      OrderItemRepository orderItemRepository) {
    this.orderItemRepository = orderItemRepository;
  }

  public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
    return orderItemRepository.findByCustomerOrder_Id(orderId);
  }

  public OrderItem getOrderItemById(Long id) {
    return orderItemRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Item do pedido não encontrado"));
  }

  public OrderItem updateOrderItem(Long itemId, Integer newQuantity) {
    if (newQuantity == null || newQuantity <= 0) {
      throw new IllegalArgumentException("A quantidade deve ser maior que zero");
    }

    OrderItem item = getOrderItemById(itemId);
    CustomerOrder order = item.getCustomerOrder();

    order.setTotalValue(order.getTotalValue().subtract(item.getSubtotal()));
    item.setQuantity(newQuantity);
    BigDecimal newSubtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity));
    item.setSubtotal(newSubtotal);

    order.setTotalValue(order.getTotalValue().add(newSubtotal));

    return orderItemRepository.save(item);
  }

  public void removeOrderItem(Long itemId) {
    OrderItem item = getOrderItemById(itemId);
    CustomerOrder order = item.getCustomerOrder();

    order.setTotalValue(order.getTotalValue().subtract(item.getSubtotal()));

    orderItemRepository.delete(item);
  }

  public List<OrderItem> getOrderItemsByProductId(Long productId) {
    return orderItemRepository.findByProduct_Id(productId);
  }

  public List<OrderItem> getAllOrderItems() {
    return orderItemRepository.findAll();
  }
}

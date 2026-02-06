package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.requests.CustomerOrderDTO;
import com.deliverytech.delivery_api.dto.requests.OrderItemDTO;
import com.deliverytech.delivery_api.dto.responses.CustomerOrderResponseDTO;
import com.deliverytech.delivery_api.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
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

  private final ModelMapper mapper;

  private CustomerOrderResponseDTO toResponseDTO(CustomerOrder order) {
    return mapper.map(order, CustomerOrderResponseDTO.class);
  }

  public CustomerOrderService(CustomerOrderRepository customerOrderRepository, CustomerRepository customerRepository,
      RestaurantRepository restaurantRepository, OrderItemRepository orderItemRepository,
      ProductRepository productRepository, ModelMapper mapper) {
    this.customerOrderRepository = customerOrderRepository;
    this.customerRepository = customerRepository;
    this.restaurantRepository = restaurantRepository;
    this.orderItemRepository = orderItemRepository;
    this.productRepository = productRepository;
    this.mapper = mapper;
  }

  @Transactional
  public CustomerOrderResponseDTO createOrder(CustomerOrderDTO orderDTO) {
    Customer customer = customerRepository.findById(orderDTO.getCustomerId())
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

    if (!customer.getActive()) {
      throw new BusinessException("Cliente inativo não pode fazer pedidos");
    }

    Restaurant restaurant = restaurantRepository.findById(orderDTO.getRestaurantId())
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
    if (!restaurant.getActive()) {
      throw new BusinessException("Restaurante inativo não pode receber pedidos");
    }

    CustomerOrder newOrder = new CustomerOrder();

    newOrder.setCustomer(customer);
    newOrder.setRestaurant(restaurant);
    newOrder.setStatus(CustomerOrderStatus.PENDENTE);
    newOrder.setAddressDelivery(orderDTO.getAddressDelivery());

    BigDecimal totalValue = BigDecimal.ZERO;

    for (OrderItemDTO itemDTO : orderDTO.getItems()) {
      Product product = productRepository.findById(itemDTO.getProductId())
          .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

      if (!product.getAvailable()) {
        throw new BusinessException("Produto sem disponibilidade não pode ser adicionado ao pedido");
      }

      if (!product.getRestaurant().getId().equals(restaurant.getId())) {
        throw new BusinessException("Produto  " + product.getName() + " não pertence ao restaurante do pedido");
      }

      OrderItem orderItem = new OrderItem();

      orderItem.setCustomerOrder(newOrder);
      orderItem.setProduct(product);

      orderItem.setQuantity(itemDTO.getQuantity());
      orderItem.setUnitPrice(product.getPrice());

      BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
      orderItem.setSubtotal(subtotal);

      newOrder.getOrderItems().add(orderItem);

      totalValue = totalValue.add(subtotal);
    }

    newOrder.setTotalValue(totalValue);

    CustomerOrder savedOrder = customerOrderRepository.save(newOrder);

    return toResponseDTO(savedOrder);
  }

  @Transactional
  public CustomerOrderResponseDTO confirmOrder(Long orderId) {
    CustomerOrder existingOrder = customerOrderRepository.findByIdWithItems(orderId);
    if (existingOrder == null) {
      throw new EntityNotFoundException("Pedido não encontrado");
    }

    if (existingOrder.getStatus() != CustomerOrderStatus.PENDENTE) {
      throw new BusinessException("Apenas pedidos pendentes podem ser confirmados");
    }

    existingOrder.setStatus(CustomerOrderStatus.CONFIRMADO);
    return toResponseDTO(existingOrder);
  }

  @Transactional
  public CustomerOrderResponseDTO updateStatusOrder(Long orderId) {
    CustomerOrder existingOrder = customerOrderRepository.findByIdWithItems(orderId);
    if (existingOrder == null) {
      throw new EntityNotFoundException("Pedido não encontrado");
    }

    CustomerOrderStatus currentStatus = existingOrder.getStatus();

    switch (currentStatus) {
      case CONFIRMADO -> existingOrder.setStatus(CustomerOrderStatus.PREPARANDO);
      case PREPARANDO -> existingOrder.setStatus(CustomerOrderStatus.A_CAMINHO);
      case A_CAMINHO -> existingOrder.setStatus(CustomerOrderStatus.ENTREGUE);
      case CANCELADO, ENTREGUE -> throw new BusinessException("Não é possível atualizar o status de um pedido " +
          "que já foi entregue ou cancelado");
      default -> throw new BusinessException("Status atual do pedido não permite atualização automática");
    }

    return toResponseDTO(existingOrder);
  }

  public List<CustomerOrderResponseDTO> getOrdersByCustomerId(Long customerId) {
    return customerOrderRepository.findByCustomerId(customerId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  public List<CustomerOrderResponseDTO> getAllOrders() {
    return customerOrderRepository.findAll()
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<CustomerOrderResponseDTO> findByCustomerIdWithItems(Long customerId) {
    return customerOrderRepository.findByCustomerIdWithItems(customerId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  public CustomerOrderResponseDTO cancelOrder(Long orderId) {
    CustomerOrder existingOrder = customerOrderRepository.findByIdWithItems(orderId);
    if (existingOrder == null) {
      throw new EntityNotFoundException("Pedido não encontrado");
    }

    if (existingOrder.getStatus() == CustomerOrderStatus.ENTREGUE) {
      throw new BusinessException("Não é possível cancelar um pedido que já foi entregue");
    }

    if (existingOrder.getStatus() == CustomerOrderStatus.A_CAMINHO) {
      throw new BusinessException("Não é possível cancelar um pedido que está a caminho");
    }

    existingOrder.setStatus(CustomerOrderStatus.CANCELADO);
    return toResponseDTO(existingOrder);
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

  public CustomerOrderResponseDTO getOrderById(Long id) {
    CustomerOrder order = customerOrderRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
    return toResponseDTO(order);
  }
}
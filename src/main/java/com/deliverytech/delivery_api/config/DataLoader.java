package com.deliverytech.delivery_api.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

@Configuration
public class DataLoader {
  @Bean
  public CommandLineRunner initData(CustomerRepository customerRepository, RestaurantRepository restaurantRepository,
      ProductRepository productRepository, CustomerOrderRepository customerOrderRepository,
      OrderItemRepository orderItemRepository) {
    return args -> {
      System.out.println("Iniciando carregamento de dados...");

      Customer customer1 = new Customer();
      customer1.setName("João Silva");
      customer1.setEmail("joao@email.com");
      customer1.setPhone("(11) 99999-1111");
      customer1.setAddress("Rua A, 123 - São Paulo/SP");
      customer1.setCreatedAt(LocalDateTime.now());
      customer1.setActive(true);

      Customer customer2 = new Customer();
      customer2.setName("Maria Santos");
      customer2.setEmail("maria@email.com");
      customer2.setPhone("(11) 99999-2222");
      customer2.setAddress("Rua B, 456 - São Paulo/SP");
      customer2.setCreatedAt(LocalDateTime.now());
      customer2.setActive(true);

      Customer customer3 = new Customer();
      customer3.setName("Pedro Oliveira");
      customer3.setEmail("pedro@email.com");
      customer3.setPhone("(11) 99999-3333");
      customer3.setAddress("Rua C, 789 - São Paulo/SP");
      customer3.setCreatedAt(LocalDateTime.now());
      customer3.setActive(true);

      List<Customer> customers = Arrays.asList(customer1, customer2, customer3);
      customerRepository.saveAll(customers);

      Restaurant restaurant1 = new Restaurant();
      restaurant1.setName("Pizzaria Bella");
      restaurant1.setCategory("Italiana");
      restaurant1.setAddress("Av. Paulista, 1000 - São Paulo/SP");
      restaurant1.setPhone("(11) 3333-1111");
      restaurant1.setDeliveryFee(BigDecimal.valueOf(5.00));
      restaurant1.setRating(BigDecimal.valueOf(4.5));
      restaurant1.setActive(true);

      Restaurant restaurant2 = new Restaurant();
      restaurant2.setName("Burger House");
      restaurant2.setCategory("Hamburgueria");
      restaurant2.setAddress("Rua Augusta, 500 - São Paulo/SP");
      restaurant2.setPhone("(11) 3333-2222");
      restaurant2.setDeliveryFee(BigDecimal.valueOf(3.50));
      restaurant2.setRating(BigDecimal.valueOf(4.2));
      restaurant2.setActive(true);

      Restaurant restaurant3 = new Restaurant();
      restaurant3.setName("Sushi Master");
      restaurant3.setCategory("Japonesa");
      restaurant3.setAddress("Rua Liberdade, 200 - São Paulo/SP");
      restaurant3.setPhone("(11) 3333-3333");
      restaurant3.setDeliveryFee(BigDecimal.valueOf(8.00));
      restaurant3.setRating(BigDecimal.valueOf(4.8));
      restaurant3.setActive(true);

      List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2, restaurant3);
      restaurantRepository.saveAll(restaurants);

      Product product1 = new Product();
      product1.setName("Pizza Margherita");
      product1.setDescription("Molho de tomate, mussarela e manjericão");
      product1.setPrice(BigDecimal.valueOf(35.90));
      product1.setCategory("Pizza");
      product1.setAvailable(true);
      product1.setRestaurant(restaurant1);

      Product product2 = new Product();
      product2.setName("Pizza Calabresa");
      product2.setDescription("Molho de tomate, mussarela e calabresa");
      product2.setPrice(BigDecimal.valueOf(38.90));
      product2.setCategory("Pizza");
      product2.setAvailable(true);
      product2.setRestaurant(restaurant1);

      Product product3 = new Product();
      product3.setName("Lasanha Bolonhesa");
      product3.setDescription("Lasanha tradicional com molho bolonhesa");
      product3.setPrice(BigDecimal.valueOf(28.90));
      product3.setCategory("Massa");
      product3.setAvailable(true);
      product3.setRestaurant(restaurant1);

      Product product4 = new Product();
      product4.setName("X-Burger");
      product4.setDescription("Hambúrguer, queijo, alface e tomate");
      product4.setPrice(BigDecimal.valueOf(18.90));
      product4.setCategory("Hambúrguer");
      product4.setAvailable(true);
      product4.setRestaurant(restaurant2);

      Product product5 = new Product();
      product5.setName("X-Bacon");
      product5.setDescription("Hambúrguer, queijo, bacon, alface e tomate");
      product5.setPrice(BigDecimal.valueOf(22.90));
      product5.setCategory("Hambúrguer");
      product5.setAvailable(true);
      product5.setRestaurant(restaurant2);

      Product product6 = new Product();
      product6.setName("Batata Frita");
      product6.setDescription("Porção de batata frita crocante");
      product6.setPrice(BigDecimal.valueOf(12.90));
      product6.setCategory("Acompanhamento");
      product6.setAvailable(true);
      product6.setRestaurant(restaurant2);

      Product product7 = new Product();
      product7.setName("Combo Sashimi");
      product7.setDescription("15 peças de sashimi variado");
      product7.setPrice(BigDecimal.valueOf(45.90));
      product7.setCategory("Sashimi");
      product7.setAvailable(true);
      product7.setRestaurant(restaurant3);

      Product product8 = new Product();
      product8.setName("Hot Roll Salmão");
      product8.setDescription("8 peças de hot roll de salmão");
      product8.setPrice(BigDecimal.valueOf(32.90));
      product8.setCategory("Hot Roll");
      product8.setAvailable(true);
      product8.setRestaurant(restaurant3);

      List<Product> products = Arrays.asList(product1, product2, product3, product4, product5, product6, product7,
          product8);
      productRepository.saveAll(products);

      CustomerOrder order1 = new CustomerOrder();
      order1.setOrderNumber("PED1234567890");
      order1.setDateOrder(LocalDateTime.now());
      order1.setStatus(CustomerOrderStatus.PENDENTE);
      order1.setTotalValue(BigDecimal.valueOf(64.80));
      order1.setAddressDelivery("Rua A, 123 - São Paulo/SP");
      order1.setDeliveryFee(BigDecimal.valueOf(5.00));
      order1.setCustomer(customer1);
      order1.setRestaurant(restaurant1);

      CustomerOrder order2 = new CustomerOrder();
      order2.setOrderNumber("PED1234567891");
      order2.setDateOrder(LocalDateTime.now());
      order2.setStatus(CustomerOrderStatus.CONFIRMADO);
      order2.setTotalValue(BigDecimal.valueOf(113.60));
      order2.setAddressDelivery("Rua B, 456 - São Paulo/SP");
      order2.setDeliveryFee(BigDecimal.valueOf(3.50));
      order2.setCustomer(customer2);
      order2.setRestaurant(restaurant2);

      CustomerOrder order3 = new CustomerOrder();
      order3.setOrderNumber("PED1234567892");
      order3.setDateOrder(LocalDateTime.now());
      order3.setStatus(CustomerOrderStatus.ENTREGUE);
      order3.setTotalValue(BigDecimal.valueOf(114.70));
      order3.setAddressDelivery("Rua C, 789 - São Paulo/SP");
      order3.setDeliveryFee(BigDecimal.valueOf(8.00));
      order3.setCustomer(customer3);
      order3.setRestaurant(restaurant3);

      List<CustomerOrder> orders = Arrays.asList(order1, order2, order3);
      customerOrderRepository.saveAll(orders);

      OrderItem item1 = new OrderItem();
      item1.setQuantity(1);
      item1.setUnitPrice(BigDecimal.valueOf(35.90));
      item1.setSubtotal(BigDecimal.valueOf(35.90));
      item1.setCustomerOrder(order1);
      item1.setProduct(product1);

      OrderItem item2 = new OrderItem();
      item2.setQuantity(1);
      item2.setUnitPrice(BigDecimal.valueOf(28.90));
      item2.setSubtotal(BigDecimal.valueOf(28.90));
      item2.setCustomerOrder(order1);
      item2.setProduct(product3);

      OrderItem item3 = new OrderItem();
      item3.setQuantity(2);
      item3.setUnitPrice(BigDecimal.valueOf(35.90));
      item3.setSubtotal(BigDecimal.valueOf(71.80));
      item3.setCustomerOrder(order2);
      item3.setProduct(product1);

      OrderItem item4 = new OrderItem();
      item4.setQuantity(1);
      item4.setUnitPrice(BigDecimal.valueOf(18.90));
      item4.setSubtotal(BigDecimal.valueOf(18.90));
      item4.setCustomerOrder(order2);
      item4.setProduct(product4);

      OrderItem item5 = new OrderItem();
      item5.setQuantity(1);
      item5.setUnitPrice(BigDecimal.valueOf(45.90));
      item5.setSubtotal(BigDecimal.valueOf(45.90));
      item5.setCustomerOrder(order3);
      item5.setProduct(product7);

      OrderItem item6 = new OrderItem();
      item6.setQuantity(1);
      item6.setUnitPrice(BigDecimal.valueOf(32.90));
      item6.setSubtotal(BigDecimal.valueOf(32.90));
      item6.setCustomerOrder(order3);
      item6.setProduct(product8);

      OrderItem item7 = new OrderItem();
      item7.setQuantity(1);
      item7.setUnitPrice(BigDecimal.valueOf(35.90));
      item7.setSubtotal(BigDecimal.valueOf(35.90));
      item7.setCustomerOrder(order3);
      item7.setProduct(product1);

      List<OrderItem> orderItems = Arrays.asList(item1, item2, item3, item4, item5, item6, item7);
      orderItemRepository.saveAll(orderItems);

      System.out.println("Carregamento de dados concluído.");
    };
  }
}
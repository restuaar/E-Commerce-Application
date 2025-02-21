package com.app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.app.entites.*;
import com.app.payloads.*;
import com.app.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  public UserRepo userRepo;

  @Autowired
  public CartRepo cartRepo;

  @Autowired
  public OrderRepo orderRepo;

  @Autowired
  public BankRepo bankRepo;

  @Autowired
  private PaymentRepo paymentRepo;

  @Autowired
  public OrderItemRepo orderItemRepo;

  @Autowired
  public CartItemRepo cartItemRepo;

  @Autowired
  public CouponRepo couponRepo;

  @Autowired
  public UserService userService;

  @Autowired
  public CartService cartService;

  @Autowired
  public ModelMapper modelMapper;

  @Override
  public OrderDTO placeOrder(String email, Long cartId, PaymentDTO paymentDTO) {

    Cart cart = cartRepo.findCartByEmailAndCartId(email, cartId);

    if (cart == null) {
      throw new ResourceNotFoundException("Cart", "cartId", cartId);
    }

    Order order = new Order();

    order.setEmail(email);
    order.setOrderDate(LocalDate.now());

    double totalAmount = cart.getTotalPrice();

    Date today = new Date();

    if (paymentDTO.getCouponId() != null) {
      Coupon coupon = couponRepo.findByCouponId(paymentDTO.getCouponId());
      if (coupon == null) {
        throw new ResourceNotFoundException("Coupon", "couponId", paymentDTO.getCouponId());
      } else if (coupon.getExpDate().before(today)) {
        throw new APIException("This coupon has expired");
      } else if (coupon.getQuantity() <= 0) {
        throw new APIException("This coupon is out of stock");
      }

      coupon.setQuantity(coupon.getQuantity() - 1);
      totalAmount = (1 - (coupon.getDiscount() / 100.0)) * cart.getTotalPrice();
    }

    order.setTotalAmount(totalAmount);
    order.setOrderStatus("Order Accepted !");

    Payment payment = new Payment();
    payment.setOrder(order);

    if (paymentDTO.getCouponId() != null) {
      payment.setCouponId(paymentDTO.getCouponId());
    }

    String paymentType = paymentDTO.getPaymentMethod().toLowerCase();
    payment.setPaymentMethod(paymentType);
    User user = userRepo.findByEmail(email).orElse(null);

    if (paymentType.equals("cash on delivery")) {
      String addressId = paymentDTO.getAddressId();
      if (paymentDTO == null || addressId == null || addressId.isEmpty()) {
        throw new APIException("You must input the address");
      } else {
        Address address = user.getAddresses().stream().filter(a -> a.getAddressId().equals(Long.parseLong(addressId)))
            .findFirst()
            .orElse(null);

        if (address == null) {
          throw new ResourceNotFoundException("Address", "addressId", addressId);
        }

        payment.setAddress(address);
      }
    } else if (paymentType.equals("bank transfer")) {
      String bankNameDTO = paymentDTO.getBankName().toLowerCase();
      Bank bank = bankRepo.findByBankName(bankNameDTO);
      if (bank == null) {
        throw new ResourceNotFoundException("Bank", "bankName", bankNameDTO);
      }

      BankDTO bankDTO = new BankDTO();
      bankDTO.setId(bank.getId());
      bankDTO.setAccountNumber(bank.getAccountNumber());
      bankDTO.setBankName(bank.getBankName());
      payment.setBank(bank);
      paymentDTO.setBank(bankDTO);

    } else if (paymentType.equals("credit card")) {
      Long cardNumber = paymentDTO.getCardNumber();
      Integer CVC = paymentDTO.getCardVerificationCode();
      if (cardNumber == null || String.valueOf(cardNumber).length() != 16) {
        throw new APIException("Card number must be 16 digits");
      }

      if (CVC == null || String.valueOf(CVC).length() != 3) {
        throw new APIException("Card verification code must be 3 digits");
      }

      payment.setCardNumber(cardNumber);
      payment.setCardVerificationCode(CVC);
    } else {
      throw new APIException("Your payment method doesn't exist");
    }

    payment = paymentRepo.save(payment);

    order.setPayment(payment);

    Order savedOrder = orderRepo.save(order);

    List<CartItem> cartItems = cart.getCartItems();

    if (cartItems.size() == 0) {
      throw new APIException("Cart is empty");
    }

    List<OrderItem> orderItems = new ArrayList<>();

    for (CartItem cartItem : cartItems) {
      OrderItem orderItem = new OrderItem();

      orderItem.setProduct(cartItem.getProduct());
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setDiscount(cartItem.getDiscount());
      orderItem.setOrderedProductPrice(cartItem.getProductPrice());
      orderItem.setOrder(savedOrder);

      orderItems.add(orderItem);
    }

    orderItems = orderItemRepo.saveAll(orderItems);

    cart.getCartItems().forEach(item -> {
      int quantity = item.getQuantity();

      Product product = item.getProduct();

      cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

      product.setQuantity(product.getQuantity() - quantity);
    });
    OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

    orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

    return orderDTO;
  }

  @Override
  public List<OrderDTO> getOrdersByUser(String email) {
    List<Order> orders = orderRepo.findAllByEmail(email);

    List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
        .collect(Collectors.toList());

    if (orderDTOs.size() == 0) {
      throw new APIException("No orders placed yet by the user with email: " + email);
    }

    return orderDTOs;
  }

  @Override
  public OrderDTO getOrder(String email, Long orderId) {

    Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

    if (order == null) {
      throw new ResourceNotFoundException("Order", "orderId", orderId);
    }

    return modelMapper.map(order, OrderDTO.class);
  }

  @Override
  public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

    Page<Order> pageOrders = orderRepo.findAll(pageDetails);

    List<Order> orders = pageOrders.getContent();

    List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
        .collect(Collectors.toList());

    if (orderDTOs.size() == 0) {
      throw new APIException("No orders placed yet by the users");
    }

    OrderResponse orderResponse = new OrderResponse();

    orderResponse.setContent(orderDTOs);
    orderResponse.setPageNumber(pageOrders.getNumber());
    orderResponse.setPageSize(pageOrders.getSize());
    orderResponse.setTotalElements(pageOrders.getTotalElements());
    orderResponse.setTotalPages(pageOrders.getTotalPages());
    orderResponse.setLastPage(pageOrders.isLast());

    return orderResponse;
  }

  @Override
  public OrderDTO updateOrder(String email, Long orderId, String orderStatus) {

    Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

    if (order == null) {
      throw new ResourceNotFoundException("Order", "orderId", orderId);
    }

    order.setOrderStatus(orderStatus);

    return modelMapper.map(order, OrderDTO.class);
  }

}

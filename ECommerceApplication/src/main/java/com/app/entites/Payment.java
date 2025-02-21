package com.app.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentId;

  @OneToOne(mappedBy = "payment", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private Order order;

  @NotBlank
  @Size(min = 4, message = "Payment method must contain atleast 4 characters")
  private String paymentMethod;

  @ManyToOne
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;
  
  private String mobileNumber;

  @ManyToOne
  @JoinColumn(name = "id")
  private Bank bank;

  private Long cardNumber;

  private Integer cardVerificationCode;
}

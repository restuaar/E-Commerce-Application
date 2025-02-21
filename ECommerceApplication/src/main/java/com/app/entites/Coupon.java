package com.app.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data
@Table(name = "coupons")
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long couponId;

  @NotBlank
  @Size(min = 5, message = "Coupon must contain atleast 5 characters")
  private String couponName;

  @NotBlank
  private String couponCode;

  @NotNull
  private double discount;

  @NotNull
  private Date expDate;

  @NotNull
  private Long quantity;
}

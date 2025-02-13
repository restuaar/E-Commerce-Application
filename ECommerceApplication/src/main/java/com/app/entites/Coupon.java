package com.app.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private String brandName;

  @NotBlank
  private double discount;

  @NotBlank
  private boolean active;
}

package com.app.entites;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "brands")
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long brandId;

  @NotBlank
  @Column(unique = true)
  @Size(min = 5, message = "Brand must contain atleast 5 characters")
  private String brandName;

  @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
  private List<Product> products;
}

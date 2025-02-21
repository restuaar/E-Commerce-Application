package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.entites.Brand;
import com.app.payloads.BrandDTO;
import com.app.services.BrandService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class BrandController {

  @Autowired
  private BrandService brandService;

  @PostMapping("/admin/brand")
  public ResponseEntity<BrandDTO> createBrand(@Valid @RequestBody Brand Brand) {
    BrandDTO savedBrand = brandService.createBrand(Brand);

    return new ResponseEntity<BrandDTO>(savedBrand, HttpStatus.CREATED);
  }

  @GetMapping("/public/brand")
  public ResponseEntity<List<BrandDTO>> getBrands() {
    List<BrandDTO> brands = brandService.getBrands();

    return new ResponseEntity<List<BrandDTO>>(brands, HttpStatus.FOUND);
  }

  @GetMapping("/public/brand/{brandId}")
  public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long brandId) {
    BrandDTO brand = brandService.getBrandById(brandId);

    return new ResponseEntity<BrandDTO>(brand, HttpStatus.FOUND);
  }
}

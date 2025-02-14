package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.entites.Brand;
import com.app.services.BrandService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class BrandController {

	@Autowired
	private BrandService BrandService;

	@PostMapping("/admin/brand")
	public ResponseEntity<Brand> createBrand(@Valid @RequestBody Brand Brand) {
		Brand savedBrand = BrandService.createBrand(Brand);

		return new ResponseEntity<Brand>(savedBrand, HttpStatus.CREATED);
	}

}

package com.app.services;

import java.util.List;

import com.app.entites.Brand;
import com.app.payloads.BrandDTO;
public interface BrandService {

  BrandDTO getBrandById(Long brandId);

  List<BrandDTO> getBrands();

	BrandDTO createBrand(Brand brand);
}

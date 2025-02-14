package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Brand;
import com.app.exceptions.APIException;
import com.app.payloads.BrandDTO;
import com.app.repositories.BrandRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class BrandServiceImpl implements BrandService {

  @Autowired
  private BrandRepo brandRepo;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public BrandDTO getBrandById(Long brandId) {
    Brand brand = brandRepo.findByBrandId(brandId);

    if (brand == null) {
      throw new APIException("Brand with the id '" + brandId + "' not found !!!");
    }

    return modelMapper.map(brand, BrandDTO.class);
  }

  public List<BrandDTO> getBrands() {
    List<Brand> brands = brandRepo.findAll();

    if (brands.isEmpty()) {
      throw new APIException("No brands found !!!");
    }

    return brands.stream().map(brand -> modelMapper.map(brand, BrandDTO.class)).collect(Collectors.toList());
  }

  @Override
  public BrandDTO createBrand(Brand brand) {
    Brand savedBrand = brandRepo.findByBrandId(brand.getBrandId());

    if (savedBrand != null) {
      throw new APIException("Brand with the name '" + brand.getBrandName() + "' already exists !!!");
    }

    savedBrand = brandRepo.save(brand);

    return modelMapper.map(savedBrand, BrandDTO.class);
  }
}

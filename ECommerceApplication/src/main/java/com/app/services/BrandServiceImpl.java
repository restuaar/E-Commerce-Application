package com.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entites.Brand;
import com.app.entites.Product;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
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
	public Brand createBrand(Brand brand) {
		Brand savedBrand = brandRepo.findByBrandId(brand.getBrandId());
        

		if (savedBrand != null) {
			throw new APIException("Brand with the name '" + brand.getBrandName() + "' already exists !!!");
		}

		savedBrand = brandRepo.save(brand);

		return modelMapper.map(savedBrand, Brand.class);
	}
}

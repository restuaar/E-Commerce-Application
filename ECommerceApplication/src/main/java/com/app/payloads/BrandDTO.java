package com.app.payloads;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {
	
	private Long brandId;
	private String brandName;
	private List<ProductDTO> products = new ArrayList<>();
}

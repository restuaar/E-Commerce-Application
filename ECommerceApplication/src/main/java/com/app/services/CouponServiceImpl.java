package com.app.services;

import com.app.entites.Coupon;
import com.app.exceptions.APIException;
import com.app.payloads.*;
import com.app.repositories.CouponRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CouponServiceImpl implements CouponService{
    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    public ModelMapper modelMapper;

    @Override
    public CouponDTO addCoupon(CouponDTO couponDTO) {
        List<Coupon> coupons = couponRepo.findAll();

        Coupon coupon = new Coupon();
        coupon.setDiscount(couponDTO.getDiscount());
        coupon.setCouponName(couponDTO.getCouponName());
        coupon.setExpDate(couponDTO.getExpDate());
        coupon.setQuantity(couponDTO.getQuantity());

        Coupon savedCoupon = couponRepo.save(coupon);
        CouponDTO finalCouponDTO = modelMapper.map(savedCoupon, CouponDTO.class);
        return finalCouponDTO;
    }

    @Override
    public List<CouponDTO> getAllCoupons() {
        List<Coupon> coupons = couponRepo.findAll();

        if (coupons.isEmpty()) {
            throw new APIException("No coupon exists");
        }

        List<CouponDTO> couponDTOS = coupons.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());

        return couponDTOS;
    }
}

package com.app.repositories;

import com.app.entites.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Long> {
    Coupon findByCouponId(Long CouponId);
    Coupon findByCouponName(String CouponId);
}

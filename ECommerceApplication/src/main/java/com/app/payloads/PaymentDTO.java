package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	private Long paymentId;
	private String paymentMethod;
	private Long couponId;
	private String addressId;
	private String bankName;
	private Long cardNumber;
	private Integer cardVerificationCode;
	private BankDTO bank;
}

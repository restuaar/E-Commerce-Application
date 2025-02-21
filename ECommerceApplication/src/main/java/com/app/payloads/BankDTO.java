package com.app.payloads;

import com.app.entites.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDTO {
    private long id;
    private String bankName;
    private long accountNumber;
}

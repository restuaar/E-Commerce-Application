package com.app.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "bank")
@NoArgsConstructor
@AllArgsConstructor

public class Bank {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private long id;
    private String bankName;
    private long accountNumber;

    @OneToMany(mappedBy = "bank", cascade =  CascadeType.ALL )
    private List<Payment> payments;
}
package com.duytv4.accounts.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private String status;
}

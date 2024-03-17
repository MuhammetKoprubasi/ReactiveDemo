package com.qred.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRecord {
  private LocalDate paymentDate;
  private BigDecimal amount;
  private PaymentType paymentType;
  private String contractNumber;
}

package com.qred.payment.model;

import com.qred.payment.dto.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("payments")
public class Payment {

  @Id
  @Column("id")
  private Long id;

  @Column("payment_date")
  private LocalDate paymentDate;

  @Column("amount")
  private BigDecimal amount;

  @Column("payment_type")
  private PaymentType paymentType;

  @Column("contract_number")
  private String contractNumber;
}

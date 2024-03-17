package com.qred.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("contracts")
public class Contract {

  @Id
  private Long id;

  @Column("contract_number")
  private String contractNumber;

  @Column("details")
  private String details;

  @Column("client_id")
  private Long clientId;
}
package com.qred.payment.dto;

import lombok.Data;

@Data
public class ContractRecord {

  private String contractNumber;
  private String details;
  private Long clientId;
}

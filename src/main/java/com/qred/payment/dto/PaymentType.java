package com.qred.payment.dto;

public enum PaymentType {
  INCOMING,
  OUTGOING;

  public static PaymentType fromString(String type) {
    for (PaymentType paymentType : values()) {
      if (paymentType.name().equalsIgnoreCase(type)) {
        return paymentType;
      }
    }
    throw new IllegalArgumentException("Unknown payment type: " + type);
  }
}

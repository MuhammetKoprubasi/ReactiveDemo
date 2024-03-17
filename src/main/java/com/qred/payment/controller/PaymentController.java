package com.qred.payment.controller;

import com.qred.payment.dto.PaymentRecord;
import com.qred.payment.model.Payment;
import com.qred.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  // Get payments for contract number
  @GetMapping("/contract/{contractNumber}")
  public Flux<Payment> getPaymentsByContractNumber(@PathVariable String contractNumber) {
    return paymentService.findByContractNumber(contractNumber);
  }

  // Create a new paymentRecord
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Payment> createPayment(@RequestBody PaymentRecord paymentRecord) {
    return paymentService.createPayment(paymentRecord);
  }

  // Update a payment
  @PutMapping("/{id}")
  public Mono<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
    return paymentService.updatePayment(id, payment);
  }

  // Delete a payment
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deletePayment(@PathVariable Long id) {
    return paymentService.deletePayment(id);
  }
}

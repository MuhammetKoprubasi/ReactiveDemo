package com.qred.payment.service;

import com.qred.payment.dto.PaymentRecord;
import com.qred.payment.model.Payment;
import com.qred.payment.repository.ContractRepository;
import com.qred.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.MathContext;

@Service
public class PaymentService {

  MathContext mc = new MathContext(2);

  private final PaymentRepository paymentRepository;
  private final ContractRepository contractRepository;

  @Autowired
  public PaymentService(PaymentRepository paymentRepository, ContractRepository contractRepository) {
    this.paymentRepository = paymentRepository;
    this.contractRepository = contractRepository;
  }

  public Flux<Payment> findByContractNumber(String contractNumber) {
    return paymentRepository.findByContractNumber(contractNumber);
  }

  public Mono<Payment> updatePayment(Long id, Payment updatedPayment) {
    return paymentRepository.findById(id)
            .flatMap(existingPayment -> {
              existingPayment.setPaymentDate(updatedPayment.getPaymentDate());
              existingPayment.setAmount(updatedPayment.getAmount());
              existingPayment.setPaymentType(updatedPayment.getPaymentType());
              existingPayment.setContractNumber(updatedPayment.getContractNumber());
              return paymentRepository.save(existingPayment);
            })
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found with id " + id)));
  }

  public Mono<Void> deletePayment(Long id) {
    // First, check if the payment exists to provide a meaningful error if it doesn't
    return paymentRepository.findById(id)
            .flatMap(existingPayment ->
                    paymentRepository.deleteById(id) // Delete if exists
            )
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found with id " + id)));
  }

  public Mono<Payment> createPayment(PaymentRecord paymentRecord) {
    return contractRepository.findByContractNumber(paymentRecord.getContractNumber())
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid contract number")))
            .then(paymentRepository.save(
                    Payment.builder()
                            .paymentType(paymentRecord.getPaymentType())
                            .paymentDate(paymentRecord.getPaymentDate())
                            .contractNumber(paymentRecord.getContractNumber())
                            .amount(paymentRecord.getAmount().abs(mc))
                            .build()));
  }

}
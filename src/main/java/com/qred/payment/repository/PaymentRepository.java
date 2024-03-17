package com.qred.payment.repository;

import com.qred.payment.model.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<Payment, Long> {

  Flux<Payment> findByContractNumber(String contractNumber);
}
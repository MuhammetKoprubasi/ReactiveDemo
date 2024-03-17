package com.qred.payment.repository;

import com.qred.payment.model.Contract;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ContractRepository extends ReactiveCrudRepository<Contract, Long> {

  Flux<Contract> findByClientId(Long clientId);

  Mono<Contract> findByContractNumber(String contractNumber);
}
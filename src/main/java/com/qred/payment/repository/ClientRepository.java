package com.qred.payment.repository;

import com.qred.payment.model.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client, Long> {

  Flux<Client> findByName(String name);

  Mono<Client> findByEmail(String email);
}
package com.qred.payment.controller;

import com.qred.payment.dto.ContractRecord;
import com.qred.payment.model.Contract;
import com.qred.payment.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/contracts")
public class ContractController {

  private final ContractService contractService;

  @Autowired
  public ContractController(ContractService contractService) {
    this.contractService = contractService;
  }

  // Create a new contractRecord
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Contract> createContract(@RequestBody ContractRecord contractRecord) {
    return contractService.createContract(contractRecord);
  }

  // Get all contracts
  @GetMapping
  public Flux<Contract> getAllContracts() {
    return contractService.getAllContracts();
  }

  // Get a single contract by ID
  @GetMapping("/{id}")
  public Mono<Contract> getContractById(@PathVariable Long id) {
    return contractService.getContractById(id);
  }

  // Update a contract
  @PutMapping("/{id}")
  public Mono<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
    return contractService.updateContract(id, contract);
  }

  // Delete a contract
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteContract(@PathVariable Long id) {
    return contractService.deleteContract(id);
  }
}

package com.qred.payment.service;

import com.qred.payment.dto.ContractRecord;
import com.qred.payment.model.Contract;
import com.qred.payment.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ContractService {

  private final ContractRepository contractRepository;

  @Autowired
  public ContractService(ContractRepository contractRepository) {
    this.contractRepository = contractRepository;
  }

  public Flux<Contract> getContractsByClientId(Long clientId) {
    return contractRepository.findByClientId(clientId);
  }

  public Mono<Contract> createContract(ContractRecord contractRecord) {
    Contract contract = Contract.builder()
            .contractNumber(contractRecord.getContractNumber())
            .details(contractRecord.getDetails())
            .clientId(contractRecord.getClientId())
            .build();
    return contractRepository.save(contract);
  }

  public Flux<Contract> getAllContracts() {
    return contractRepository.findAll();
  }

  public Mono<Contract> getContractById(Long id) {
    return contractRepository.findById(id);
  }

  public Mono<Contract> updateContract(Long id, Contract contract) {
    return contractRepository.findById(id)
            .flatMap(existingContract -> {
              existingContract.setContractNumber(contract.getContractNumber());
              existingContract.setDetails(contract.getDetails());
              existingContract.setClientId(contract.getClientId());
              return contractRepository.save(existingContract);
            });
  }

  public Mono<Void> deleteContract(Long id) {
    return contractRepository.deleteById(id);
  }
}
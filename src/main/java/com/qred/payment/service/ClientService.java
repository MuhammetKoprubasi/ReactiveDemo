package com.qred.payment.service;

import com.qred.payment.dto.ClientRecord;
import com.qred.payment.model.Client;
import com.qred.payment.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientService {

  private final ClientRepository clientRepository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public Flux<Client> getClientsByName(String name) {
    return clientRepository.findByName(name);
  }

  public Mono<Client> getClientByEmail(String email) {
    return clientRepository.findByEmail(email);
  }

  public Mono<Client> createClient(ClientRecord clientRecord) {
    Client client = Client.builder()
            .name(clientRecord.getName())
            .email(clientRecord.getEmail())
            .build();
    return clientRepository.save(client);
  }

  public Flux<Client> getAllClients() {
    return clientRepository.findAll();
  }

  public Mono<Client> getClientById(Long id) {
    return clientRepository.findById(id);
  }

  public Mono<Client> updateClient(Long id, Client client) {
    return clientRepository.findById(id)
            .flatMap(existingClient -> {
              existingClient.setName(client.getName());
              existingClient.setEmail(client.getEmail());
              return clientRepository.save(existingClient);
            });
  }

  public Mono<Void> deleteClient(Long id) {
    return clientRepository.deleteById(id);
  }
}

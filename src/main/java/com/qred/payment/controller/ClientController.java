package com.qred.payment.controller;

import com.qred.payment.dto.ClientRecord;
import com.qred.payment.model.Client;
import com.qred.payment.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clients")
public class ClientController {

  private final ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  // Create a new client
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Client> createClient(@RequestBody ClientRecord clientRecord) {
    return clientService.createClient(clientRecord);
  }

  // Get all clients
  @GetMapping
  public Flux<Client> getAllClients() {
    return clientService.getAllClients();
  }

  // Get a single client by ID
  @GetMapping("/{id}")
  public Mono<Client> getClientById(@PathVariable Long id) {
    return clientService.getClientById(id);
  }

  // Update a client
  @PutMapping("/{id}")
  public Mono<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
    return clientService.updateClient(id, client);
  }

  // Delete a client
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteClient(@PathVariable Long id) {
    return clientService.deleteClient(id);
  }
}

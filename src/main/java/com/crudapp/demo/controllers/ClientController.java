package com.crudapp.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crudapp.demo.entities.Client;
import com.crudapp.demo.services.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        Client saveClient = clientService.addClient(client);
        return ResponseEntity.ok(saveClient);
    }



        @GetMapping("/{id}")
    public ResponseEntity<Client> getProductById(@PathVariable("id") Long id) {
        final Client clientById = clientService.getClientById(id);
        return ResponseEntity.ok(clientById);
    }

            @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(id, client);
        return ResponseEntity.ok(updatedClient);
    }
            @DeleteMapping("/{id}")
    public ResponseEntity<Client> deleteProduct(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}

package com.crudapp.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crudapp.demo.entities.Client;
import com.crudapp.demo.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }
    
    public Client getClientById(Long id) {
        return clientRepository.findClientById(id);
    }
 public Client updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id)
                .map(existingClient -> {
                    existingClient.setFirstName(updatedClient.getFirstName());
                    existingClient.setLastName(updatedClient.getLastName());
                    existingClient.setMobile(updatedClient.getMobile());
                    return clientRepository.save(existingClient);
                })
                .orElseThrow(() -> new RuntimeException("Client not found with id " + id));
    }
    public void deleteClient(Long id) {
         clientRepository.deleteById(id);
    }

}

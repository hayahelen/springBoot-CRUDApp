package com.crudapp.demo.repositories;

import com.crudapp.demo.entities.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
        public Client findClientById(Long id);

}

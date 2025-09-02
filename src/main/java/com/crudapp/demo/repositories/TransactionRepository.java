package com.crudapp.demo.repositories;

import com.crudapp.demo.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySaleId(Long saleId);
}

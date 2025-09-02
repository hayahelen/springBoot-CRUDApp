package com.crudapp.demo.services;

import com.crudapp.demo.entities.Sale;
import com.crudapp.demo.entities.Transaction;
import com.crudapp.demo.repositories.SaleRepository;
import com.crudapp.demo.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SaleRepository saleRepository;

    public TransactionService(TransactionRepository transactionRepository, SaleRepository saleRepository) {
        this.transactionRepository = transactionRepository;
        this.saleRepository = saleRepository;
    }

    @Transactional
    public Transaction updateTransaction(Long transactionId, Integer quantity, Double price) {
        Transaction t = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));

        if (quantity != null) t.setQuantity(quantity);
        if (price != null) t.setPrice(price);

        transactionRepository.save(t);

        Sale sale = t.getSale();
        List<Transaction> transactions = transactionRepository.findAllBySaleId(sale.getId());
        double total = transactions.stream()
                .mapToDouble(tr -> tr.getPrice() * tr.getQuantity())
                .sum();
        sale.setTotal(total);

        saleRepository.save(sale);

        return t;
    }

    @Transactional
    public Transaction addTransactionToSale(Long saleId, Transaction newTransaction) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found: " + saleId));

        newTransaction.setSale(sale);
        Transaction savedTransaction = transactionRepository.save(newTransaction);

        List<Transaction> transactions = transactionRepository.findAllBySaleId(saleId);
        double total = transactions.stream()
                .mapToDouble(tr -> tr.getPrice() * tr.getQuantity())
                .sum();
        sale.setTotal(total);

        saleRepository.save(sale);

        return savedTransaction;
    }

    @Transactional
    public void removeTransaction(Long transactionId) {
        Transaction t = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));

        Sale sale = t.getSale();
        transactionRepository.delete(t);

        List<Transaction> transactions = transactionRepository.findAllBySaleId(sale.getId());
        double total = transactions.stream()
                .mapToDouble(tr -> tr.getPrice() * tr.getQuantity())
                .sum();
        sale.setTotal(total);

        saleRepository.save(sale);
    }

    public List<Transaction> getTransactionsBySale(Long saleId) {
        return transactionRepository.findAllBySaleId(saleId);
    }
}

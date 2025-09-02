package com.crudapp.demo.controllers;

import com.crudapp.demo.entities.Transaction;
import com.crudapp.demo.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequest request) {
        Transaction updated = transactionService.updateTransaction(id, request.getQuantity(), request.getPrice());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/sale/{saleId}")
    public List<Transaction> getTransactionsBySale(@PathVariable Long saleId) {
        return transactionService.getTransactionsBySale(saleId);
    }

    public static class TransactionRequest {
        private Integer quantity;
        private Double price;

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }
}

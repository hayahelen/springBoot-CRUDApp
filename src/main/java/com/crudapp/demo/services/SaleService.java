package com.crudapp.demo.services;

import com.crudapp.demo.dto.SaleProductDTO;
import com.crudapp.demo.entities.Client;
import com.crudapp.demo.entities.Product;
import com.crudapp.demo.entities.Sale;
import com.crudapp.demo.entities.Transaction;
import com.crudapp.demo.repositories.ClientRepository;
import com.crudapp.demo.repositories.ProductRepository;
import com.crudapp.demo.repositories.SaleRepository;
import com.crudapp.demo.repositories.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
        private final TransactionService transactionService;


    public SaleService(SaleRepository saleRepository,
                       ProductRepository productRepository,
                       TransactionRepository transactionRepository,
                       ClientRepository clientRepository,
                       TransactionService transactionService) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.transactionService = transactionService;
    }

    public List<Sale> getAllSales() {
        log.debug("Fetching all sales from database");
        List<Sale> sales = saleRepository.findAll();
        log.info("Fetched {} sales from database", sales.size());
        return sales;
    }

    public Optional<Sale> getSaleById(Long id) {
        log.debug("Fetching sale with ID {}", id);
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isPresent()) {
            log.info("Sale with ID {} found", id);
        } else {
            log.warn("Sale with ID {} not found", id);
        }
        return sale;
    }

    public Sale createSale(Sale sale, List<SaleProductDTO> products) {
        log.info("Creating new sale for seller [{}] and client [{}]",
                 sale.getSeller(), sale.getClient() != null ? sale.getClient().getId() : "null");

        if (sale.getClient() != null && sale.getClient().getId() != null) {
            Client fullClient = clientRepository.findById(sale.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client not found with id " + sale.getClient().getId()));
            sale.setClient(fullClient);
        }

        List<Transaction> transactions = new ArrayList<>();
        double total = 0;

        if (products != null && !products.isEmpty()) {
            for (SaleProductDTO dto : products) {
                Product product = productRepository.findById(dto.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));

                Transaction t = new Transaction();
                t.setProductId(product.getId());
                t.setQuantity(dto.getQuantity());
                t.setPrice(dto.getPrice() != null ? dto.getPrice() : product.getPrice());
                t.setSale(sale);

                transactions.add(t);
                total += t.getPrice() * t.getQuantity();
            }
        }

        sale.setTotal(total);
        Sale savedSale = saleRepository.save(sale);
        transactionRepository.saveAll(transactions);
        savedSale.setTransactions(transactions);

        log.info("Sale with ID {} created with total={} and {} transactions",
                 savedSale.getId(), savedSale.getTotal(), transactions.size());

        return savedSale;
    }

  
@Transactional
public Sale updateSale(Long saleId, List<SaleProductDTO> updatedProducts, Sale updatedSaleData) {
    Sale existingSale = saleRepository.findById(saleId)
            .orElseThrow(() -> new RuntimeException("Sale not found: " + saleId));

    log.info("Updating sale ID {}", saleId);

    if (updatedSaleData.getSeller() != null) existingSale.setSeller(updatedSaleData.getSeller());

    if (updatedSaleData.getClient() != null) {
        Client client = clientRepository.findById(updatedSaleData.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found: " + updatedSaleData.getClient().getId()));
        existingSale.setClient(client);
    }

    if (!existingSale.getTransactions().isEmpty()) {
        transactionRepository.deleteAll(existingSale.getTransactions());
        existingSale.getTransactions().clear();
    }

    List<Transaction> newTransactions = new ArrayList<>();
    double total = 0;

    if (updatedProducts != null && !updatedProducts.isEmpty()) {
        for (SaleProductDTO dto : updatedProducts) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));

            Transaction t = new Transaction();
            t.setProductId(product.getId());
            t.setQuantity(dto.getQuantity());
            t.setPrice(dto.getPrice() != null ? dto.getPrice() : product.getPrice());
            t.setSale(existingSale); 

            newTransactions.add(t);
            total += t.getPrice() * t.getQuantity();
        }

        transactionRepository.saveAll(newTransactions);
    }

    existingSale.setTransactions(newTransactions);
    existingSale.setTotal(total);

    log.info("Sale ID {} updated successfully with total={} and {} transactions",
            existingSale.getId(), total, newTransactions.size());

    return saleRepository.save(existingSale);
}

@Transactional
public void deleteSale(Long saleId) {
    Sale sale = saleRepository.findById(saleId)
            .orElseThrow(() -> new RuntimeException("Sale not found: " + saleId));

    if (!sale.getTransactions().isEmpty()) {
        transactionRepository.deleteAll(sale.getTransactions());
    }

    saleRepository.delete(sale);

    log.info("Sale with ID {} and its transactions have been deleted", saleId);
}
}
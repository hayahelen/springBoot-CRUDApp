package com.crudapp.demo.controllers;

import com.crudapp.demo.dto.SaleProductDTO;
import com.crudapp.demo.entities.Sale;
import com.crudapp.demo.services.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody SaleRequest request) {
        Sale saved = saleService.createSale(request.getSale(), request.getProducts());
        return ResponseEntity.ok(saved);
    }

    public static class SaleRequest {
        private Sale sale;
        private List<SaleProductDTO> products;

        public Sale getSale() { return sale; }
        public void setSale(Sale sale) { this.sale = sale; }

        public List<SaleProductDTO> getProducts() { return products; }
        public void setProducts(List<SaleProductDTO> products) { this.products = products; }
    }

    @PutMapping("/{id}")
public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody SaleUpdateRequest request) {
    Sale sale = saleService.updateSale(id, request.getProducts(), request.getSale());
    return ResponseEntity.ok(sale);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
    saleService.deleteSale(id);
    return ResponseEntity.noContent().build(); 
}

public static class SaleUpdateRequest {
    private Sale sale;
    private List<SaleProductDTO> products;

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    public List<SaleProductDTO> getProducts() { return products; }
    public void setProducts(List<SaleProductDTO> products) { this.products = products; }
}
}

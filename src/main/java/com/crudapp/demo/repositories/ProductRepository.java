package com.crudapp.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crudapp.demo.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Product findProductByName(String name);
    public Product findProductById(Long id);
}

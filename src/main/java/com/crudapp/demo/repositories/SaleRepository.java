package com.crudapp.demo.repositories;

import com.crudapp.demo.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}

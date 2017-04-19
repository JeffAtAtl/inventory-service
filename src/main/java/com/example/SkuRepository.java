package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    Optional<Sku> findBySku(String sku);
}

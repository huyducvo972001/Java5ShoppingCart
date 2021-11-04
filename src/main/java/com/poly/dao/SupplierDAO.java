package com.poly.dao;

import com.poly.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierDAO extends JpaRepository<Supplier, Integer> {
}

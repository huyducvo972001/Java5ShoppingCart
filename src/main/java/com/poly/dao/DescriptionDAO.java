package com.poly.dao;

import com.poly.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptionDAO extends JpaRepository<Description, Integer> {
}

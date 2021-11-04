package com.poly.dao;


import com.poly.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    @Query(value = "select * from orders",
            nativeQuery = true )
    List<Order> findAllCustomerId();
}

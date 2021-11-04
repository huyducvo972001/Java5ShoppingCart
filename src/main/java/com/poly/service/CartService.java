package com.poly.service;

import com.poly.entity.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.Collection;

public interface CartService {
    void add(OrderDetail item);
    void remove(Integer id);
    OrderDetail update(Integer id, int qty);
    void clear();
    Collection<OrderDetail> getItems();
    int getCount();
    double getAmount();
    int quantityProductInCart();
}


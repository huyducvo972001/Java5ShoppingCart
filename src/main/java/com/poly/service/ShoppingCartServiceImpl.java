package com.poly.service;

import com.poly.entity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SessionScope
@Service
public class ShoppingCartServiceImpl implements CartService{
    Map<Integer, OrderDetail> map = new HashMap<>();

    @Override
    public Collection<OrderDetail> getItems(){
        return map.values();
    }

    @Override
    public void add(OrderDetail item) {
        OrderDetail existingItem = map.get(item.getProduct().getId());

        if(existingItem != null){
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        }else{
            map.put(item.getProduct().getId(), item);
        }
    }

    @Override
    public void remove(Integer id) {
        map.remove(id);
    }
    @Override
    public OrderDetail update(Integer id, int qty) { //Update số lượng
        OrderDetail item = map.get(id);
        item.setQuantity(qty);
        return item;
    }
    @Override
    public void clear() { //Xóa hết
        map.clear();
    }
    @Override
    public int getCount() {
        return map.values().stream().mapToInt(item -> item.getQuantity()).sum();
    }
    @Override
    public double getAmount() {
        return map.values().stream().mapToDouble(OrderDetail::getPrice).sum();
    }

    @Override
    public int quantityProductInCart() {
        return getItems().size();
    }
}

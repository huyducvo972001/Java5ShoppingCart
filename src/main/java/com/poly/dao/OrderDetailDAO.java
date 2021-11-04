package com.poly.dao;

import com.poly.entity.OrderDetail;
import com.poly.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailDAO extends JpaRepository<OrderDetail, Long> {

    @Query("select new Report(o.product.category ,sum(o.quantity), sum(o.price * " +
            "o.quantity)) from OrderDetail o GROUP BY o.product.category")
    List<Report> findThongKe();

    @Query("select new Report(o.order ,sum(o.quantity), sum(o.price * " +
            "o.quantity)) from OrderDetail o GROUP BY o.order")
    List<Report> findThongKess();
}

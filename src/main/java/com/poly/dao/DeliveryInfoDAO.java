package com.poly.dao;

import com.poly.entity.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryInfoDAO extends JpaRepository<DeliveryInfo, Integer> {
    @Query(value = "select top 1 * from delivery_info order by id desc",
            nativeQuery = true )
    List<DeliveryInfo> findNewDelivery();
}

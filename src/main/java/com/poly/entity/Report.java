package com.poly.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    private Serializable group;
    private long quantity;
    private double priceTotal;

    @Override
    public String toString() {
        return "Report{" +
                "group=" + group +
                ", quantity=" + quantity +
                ", priceTotal=" + priceTotal +
                '}';
    }
}

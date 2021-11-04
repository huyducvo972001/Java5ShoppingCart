package com.poly.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "origins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Origin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "origin")
    private List<Product> products;

    public void addProduct(Product product) {
        if (products == null)
            products = new ArrayList<>();
        products.add(product);
        product.setOrigin(this);
    }

    @Override
    public String toString() {
        return "Origin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

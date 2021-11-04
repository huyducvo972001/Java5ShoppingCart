package com.poly.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String slug;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public void addProduct(Product product) {
        if (products == null)
            products = new ArrayList<>();
        products.add(product);
        product.setCategory(this);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

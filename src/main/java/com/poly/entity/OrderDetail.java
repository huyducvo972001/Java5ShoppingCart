package com.poly.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double price;


    @Override
    public String toString() {
        return "\n---------------\n" +
                "Id: " + id + "\n" +

                "Product: " + product + "\n" +
                "Quantity: " + quantity + "\n" +
                "Price: " + price + "\n" +
                "---------------\n";
    }
}

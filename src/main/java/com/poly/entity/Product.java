package com.poly.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Double discount;

    @NotNull
    private Integer quantity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate;

    private String image;

    @ManyToOne
    @JoinColumn(name = "origin_id")
    private Origin origin;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<Description> descriptions;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", quantity=" + quantity +
                ", importDate=" + importDate +
                ", image='" + image + '\'' +
                ", origin=" + origin +
                ", supplier=" + supplier +
                ", category=" + category +
                ", descriptions=" + descriptions +
                '}';
    }
}

package com.poly.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String note;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    // Important!! Need cascade type = PERSIST/ALL
    // to save/persist order details accompany to order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_info_id")
	private DeliveryInfo deliveryInfo;

    public void addCartDetail(OrderDetail detail) {
        if (orderDetails == null) orderDetails = new ArrayList<>();
        orderDetails.add(detail);
        detail.setOrder(this);
    }

    @Override
    public String toString() {
        return "Order: \n" +
                "========================\n" +
                "Id: " + id + "\n" +
                "Note: " + note + "\n" +
                "purchaseDate: " + purchaseDate + "\n" +
                "Customer: " + customer.getFirstName() + "\n" +
                "Details: \n" + orderDetails + "\n" +
                "=========================\n";
    }
}

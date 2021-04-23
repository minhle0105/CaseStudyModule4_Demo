package com.casestudy.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private CartItem cartItem;
}

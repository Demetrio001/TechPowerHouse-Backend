package com.example.techpowerhousebackend.cartDetail;

import com.example.techpowerhousebackend.card.Card;
import com.example.techpowerhousebackend.cart.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "cart_details")
@Data
@NoArgsConstructor
public class CartDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = Card.class)
    @JoinColumn(name = "card")
    private Card card;

    @Column(name = "price")
    @NotNull
    private float price;

    @Column(name = "quantity")
    @Positive
    @NotNull
    private int quantity;

    @ManyToOne(targetEntity = Cart.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart")
    @JsonIgnore
    @ToString.Exclude
    private Cart cart;

    @Column(name = "subtotal")
    @NotNull
    @Positive
    private float subTotal;

    @Version
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "version")
    private long version;

    public CartDetail(Cart cart, Card card) {
        this.cart = cart;
        this.card = card;
        this.price = card.getPrice();
        this.quantity = 1;
        this.subTotal = card.getPrice();
    }

}

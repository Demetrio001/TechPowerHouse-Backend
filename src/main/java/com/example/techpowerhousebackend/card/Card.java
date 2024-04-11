package com.example.techpowerhousebackend.card;

import com.example.techpowerhousebackend.cartDetail.CartDetail;
import com.example.techpowerhousebackend.orderDetail.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "card")
@Data
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "creator")
    @NotBlank
    private String creator;

    @Column(name = "category")
    @NotBlank
    private String category;

    @Column(name = "price")
    @NotNull
    @Positive
    private float price;

    @Column(name = "quantity")
    @NotNull
    @PositiveOrZero
    private int quantity;

    @Version
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "version")
    private long version;

    @OneToMany(targetEntity = OrderDetail.class, mappedBy = "card", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<OrderDetail> orderDetails;

    @OneToMany(targetEntity = CartDetail.class, mappedBy = "card", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<CartDetail> cartDetails;

}
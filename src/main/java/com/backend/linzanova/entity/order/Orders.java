package com.backend.linzanova.entity.order;

//import com.backend.linzanova.entity.order.Item;
import com.backend.linzanova.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Entity
@Table(name = "ORDERS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int totalSumm;
    private int priceToPayAfterDelivery;
    private int priceToPayNow;

    private String createdAt;

    private String lastName;
    private String firstName;
    private String email;
    private String patronymic;
    private String phone;
    private String customerComment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "orders_id")
    private List<Item> items;

    @Lob
    private String properties;


    private String meestTrackingId;
    private String novaPoshtaTTN;

    private boolean delivered;
    private boolean canceled;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;


}

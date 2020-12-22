package com.backend.linzanova.entity.special;


import com.backend.linzanova.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Special {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int category;

    private String name;

    private int firstItemQuanity;
    private int secondItemQuanity;

    private String firstItemName;
    private String secondItemName;

    private int price;
    private String alensaLink;

    private boolean hasAxis;
    private boolean hasCylinder;
    private boolean hasDefaultBC;
    private float defaultBC;
    private float defaultDiameter;

    private boolean activeStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

}

package com.backend.linzanova.entity.special;


import com.backend.linzanova.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Special {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    @NotBlank
    private String name;
    private int category;
    @NotNull
    private int price;
    private int avgPriceInUkraine;

    private int firstItemQuanity;
    private int secondItemQuanity;

    private String firstItemName;
    private String secondItemName;

    private boolean hasAxis;
    private boolean hasCylinder;
    private boolean hasDefaultBC;
    private float defaultBC;
    private float defaultDiameter;

    private boolean activeStatus;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> photo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}

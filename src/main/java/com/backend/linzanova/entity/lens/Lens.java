package com.backend.linzanova.entity.lens;

import com.backend.linzanova.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    @NotBlank
    private String name;
    private int category;
    @NotNull
    @Min(10)
    private int price;
    private int avgPriceInUkraine;
    private String lenseType;
    private String lenseProducer;
    private String lenseBrand;
    private int quantity;
    private String lenseCorrection;
    private String lenseMaterial;
    @Column(columnDefinition = "TEXT")
    private String description;

    private boolean hasDefaultBC;
    private boolean hasAxis;
    private boolean hasCylinder;
    private float defaultBC;
    private float defaultDiameter;
    private int lenseWater;

    private int sDate;
    private int sTdt;

    private boolean lenseSleep;

    private boolean availability;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

}

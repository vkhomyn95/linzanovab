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
    @Column
    @NotBlank
    private String name;
    @Column
    @NotNull
    @Min(10)
    private int price;
    private int category;
    @Column
    private String lenseType;
    @Column
    private String lenseProducer;
    @Column
    private String lenseBrand;
    @Column
    private int quantity;
    @Column
    private String lenseCorrection;
    @Column
    private String lenseMaterial;
    private String description;
    private int lenseWater;
    private int sDate;
    private int sTdt;
    private int lenseShelfLife;
    private boolean lenseSleep;
    private String alensaLink;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

}

package com.backend.linzanova.entity.lens;

import com.backend.linzanova.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> photo;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}

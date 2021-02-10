package com.backend.linzanova.entity.solution;

import com.backend.linzanova.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    @NotBlank
    private String name;
    private int category;
    @Min(10)
    @NotNull
    private int price;
    private int avgPriceInUkraine;

    @NotBlank
    private String solutionType;
    @NotBlank
    private String solutionProducer;
    @NotBlank
    private String solutionBrand;
    @NotNull
    private int solutionValue;
    @Column(columnDefinition = "TEXT", length = 3000)
    private String description;

    private int sDate;
    private int sTdt;

    private boolean boolHyaluronate;
    private boolean availability;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> photo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}

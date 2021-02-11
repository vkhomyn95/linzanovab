package com.backend.linzanova.entity.drops;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(name="Drops.allJoins", includeAllAttributes = true),
        @NamedEntityGraph(name="Drops.noJoins", attributeNodes = {
                @NamedAttributeNode("comments")
        })
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Drops {

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

    private String cProducer;
    private int cValue;
    @Lob
    private String description;
    private int sdate;
    private int stdt;

    private boolean availability;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    private List<String> photo;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "drops")
    private List<DropsComments> comments;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}

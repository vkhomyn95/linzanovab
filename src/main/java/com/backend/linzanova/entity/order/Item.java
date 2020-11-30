package com.backend.linzanova.entity.order;
import com.backend.linzanova.entity.drops.Drops;
import com.backend.linzanova.entity.lens.Lens;
import com.backend.linzanova.entity.special.Special;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Entity
@Table(name = "ITEMS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @JoinColumn(referencedColumnName = "lens_id")
    @ManyToMany(fetch = FetchType.LAZY)
    public List<Lens> lenses;

    @JoinColumn(referencedColumnName = "drops_id")
    @ManyToMany(fetch = FetchType.LAZY)
    public List<Drops> drops;


    @JoinColumn(referencedColumnName = "special_id")
    @ManyToMany(fetch = FetchType.LAZY)
    public List<Special> offers;



}

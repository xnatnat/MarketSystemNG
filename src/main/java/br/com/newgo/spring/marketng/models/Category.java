package br.com.newgo.spring.marketng.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "sng_categories")
@Getter
@Setter
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy = "categories")
    private Set<Product> products;

}

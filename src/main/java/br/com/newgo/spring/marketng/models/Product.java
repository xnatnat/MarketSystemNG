package br.com.newgo.spring.marketng.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sng_products")
@Getter
@Setter
public class Product extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, unique = true, length = 12)
    private String upc; //Universal Product Code
    @Column
    private String imageName;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "product")
    private Set<ProductList> productLists;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;
}
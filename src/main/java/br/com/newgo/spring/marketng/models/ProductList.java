package br.com.newgo.spring.marketng.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "sng_productList")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductList extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false)
    private Long quantity;
    @Column(nullable = false)
    private Double total;
    @ManyToOne
    @JoinColumn(name = "marketList_id", nullable = false)
    private MarketList marketList;
}

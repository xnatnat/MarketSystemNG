package br.com.newgo.spring.marketng.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "sng_marketList")
public class MarketList extends BaseEntity {
    @Column(nullable = false)
    private LocalDateTime registrationDate;
    @OneToMany(mappedBy = "marketList")
    private Set<ProductList> products;
    @ManyToOne
    @JoinColumn(name = "marketList_id", nullable = false)
    private User user;
}

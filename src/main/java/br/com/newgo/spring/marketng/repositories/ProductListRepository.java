package br.com.newgo.spring.marketng.repositories;

import br.com.newgo.spring.marketng.models.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductListRepository extends JpaRepository<ProductList, UUID> {
}

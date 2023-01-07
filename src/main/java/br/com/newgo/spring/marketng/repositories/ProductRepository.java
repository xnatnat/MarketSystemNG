package br.com.newgo.spring.marketng.repositories;

import br.com.newgo.spring.marketng.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByUpc(String upc);
    List<Product> findByDescriptionContainingIgnoreCase(String description);

    List<Product> findByNameContainingIgnoreCase(String name);
    Optional<Product> findByUpc(String productUpc);

}

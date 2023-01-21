package br.com.newgo.spring.marketng.repositories;

import br.com.newgo.spring.marketng.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>,
                                            JpaSpecificationExecutor<Product> {
    boolean existsByUpc(String upc);
    Optional<Product> findByUpc(String productUpc);

}

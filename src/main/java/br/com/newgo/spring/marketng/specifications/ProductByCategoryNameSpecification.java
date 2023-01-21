package br.com.newgo.spring.marketng.specifications;

import br.com.newgo.spring.marketng.models.Category;
import br.com.newgo.spring.marketng.models.Product;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class ProductByCategoryNameSpecification implements Specification<Product> {
    private String categoryName;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (categoryName == null)
            return null;
        Join<Product, Category> productCategoryJoin = root.join("categories");
        return criteriaBuilder.like(criteriaBuilder.lower(
                productCategoryJoin.get("name")), "%" + categoryName.toLowerCase() + "%");
    }
}
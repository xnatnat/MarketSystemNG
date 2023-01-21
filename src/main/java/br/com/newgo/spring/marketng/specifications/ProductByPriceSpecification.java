package br.com.newgo.spring.marketng.specifications;

import br.com.newgo.spring.marketng.models.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class ProductByPriceSpecification implements Specification<Product> {
    private Double priceMin;
    private Double priceMax;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (priceMin == null && priceMax == null)
            return null;
        if (priceMin != null && priceMax != null)
            return criteriaBuilder.between(root.get("price"), priceMin, priceMax);
        if (priceMin != null)
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin);
        return criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax);
    }
}

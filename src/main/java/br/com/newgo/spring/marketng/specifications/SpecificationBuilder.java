package br.com.newgo.spring.marketng.specifications;

import br.com.newgo.spring.marketng.dtos.ProductDtos.ProductFiltersDto;
import br.com.newgo.spring.marketng.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SpecificationBuilder {

    private final ProductFiltersDto filters;

    public Specification<Product> build(){

        List<Specification<Product>> specs = new ArrayList<>();

        if(filters.getName() != null)
            specs.add(new ProductByNameSpecification(filters.getName()));
        if(filters.getDescription() != null)
            specs.add(new ProductByDescriptionSpecification(filters.getDescription()));
        if(filters.getCategory() != null)
            specs.add(new ProductByCategoryNameSpecification(filters.getCategory()));

        return specs.stream()
                .reduce(Specification::or)
                .orElse(Specification.where(null));
//
//        if(filters.getPriceMin() != null || filters.getPriceMax() != null)
//            specs.add(new ProductByPriceSpecification(filters.getPriceMin(), filters.getPriceMax()));

    }
}

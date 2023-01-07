package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.ProductListDto;
import br.com.newgo.spring.marketng.exceptions.ResourceNotFoundException;
import br.com.newgo.spring.marketng.models.ProductList;
import br.com.newgo.spring.marketng.models.MarketList;
import br.com.newgo.spring.marketng.repositories.ProductListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductListService {
    final ProductListRepository productListRepository;
    final ProductService productService;
    public ProductListService(ProductListRepository productListRepository, ProductService productService) {
        this.productListRepository = productListRepository;
        this.productService = productService;
    }
    @Transactional
    public ProductList save(ProductList productList) {
        return productListRepository.save(productList);
    }

    public Set<ProductList> saveProductListAndReturnProductList(MarketList marketList, Set<ProductListDto> products) {
        if (marketList.getProducts() != null) {
            deleteProductListsNotIncludedInNewList(marketList.getProducts(), products);
        }
        Set<ProductList> productLists = new HashSet<>(products.size());

        products.stream()
                .map(item -> {
                    if (item.getId() == null) {
                        return createProductList(item, marketList);
                    } else {
                        return updateProductListIfQuantityChanged(item, marketList);
                    }
                })
                .forEach(productLists::add);
        return productLists;
    }

    private Set<ProductList> deleteProductListsNotIncludedInNewList(Set<ProductList> oldProductList,
                                                                    Set<ProductListDto> newProductList) {
        // Remove os itens que não estão presentes na nova lista de produtos
        oldProductList.removeIf(oldP -> {
            boolean isContained = newProductList.stream()
                    .noneMatch(newP -> oldP.getId().equals(newP.getId()));
            if (isContained) {
                delete(oldP);
            }
            return isContained;
        });
        return oldProductList;
    }

    private void delete(ProductList productList){
        productListRepository.delete(productList);
    }

    private ProductList createProductList(ProductListDto item, MarketList marketList){
        var product = productService.findProductOrThrow(item.getProductUpc());
        return save(new ProductList(product, item.getQuantity(), marketList));
    }

    private ProductList updateProductListIfQuantityChanged(ProductListDto item, MarketList marketList) {
        var productList = findOrThrow(item.getId());
        if (productList.getQuantity() != item.getQuantity()) {
            productList.setQuantity(item.getQuantity());
            return save(productList);
        }
        return productList;
    }

    private ProductList findOrThrow(UUID id) {
        Optional<ProductList> productList = findById(id);
        return productList.orElseThrow(
                () -> new ResourceNotFoundException("ProductList not found."));
    }

    protected Optional<ProductList> findById(UUID id){
        return productListRepository.findById(id);
    }
}

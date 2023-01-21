package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.CategoryDtos.CategoryIdDto;
import br.com.newgo.spring.marketng.dtos.ProductDtos.*;
import br.com.newgo.spring.marketng.exceptions.ResourceAlreadyExistsException;
import br.com.newgo.spring.marketng.models.Product;
import br.com.newgo.spring.marketng.repositories.ProductRepository;
import br.com.newgo.spring.marketng.specifications.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import br.com.newgo.spring.marketng.exceptions.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
@Service
public class ProductService {
    final ProductRepository productRepository;
    final ModelMapper modelMapper;
    final StorageService storageService;
    final CategoryService categoryService;

    public ProductService(ProductRepository productRepository,
                          ModelMapper modelMapper,
                          StorageService storageService,
                          CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.storageService = storageService;
        this.categoryService = categoryService;
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    public ReturnProductDto requestSaveProduct(CreateProductDto createProductDto) {
        throwIfProductExists(createProductDto.getUpc());
        return mapToDto(
                save(
                        mapToProduct(createProductDto)));
    }

    public ReturnProductDto saveImage(MultipartFile image, String upc) throws IOException {
        var product = findProductOrThrow(upc);
        product.setImageName(storageService.storeFile(image));
        return mapToDto(
                save(
                        product));
    }

    public Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }
    public Page<ReturnProductDto> findAllAndReturnDto(Pageable pageable) {
        return productListToDto(findAll(pageable));
    }

    private Page<ReturnProductDto> productListToDto(Page<Product> products) {
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No product found.");
        }
        return products.map(this::mapToDto);
    }
    public ReturnProductDto findProductAndReturnDto(String upc) {
        return mapToDto(findProductOrThrow(upc));
    }

    public Page<ProductRepresentationDto> findByFiltersAndReturnDto(ProductFiltersDto productFiltersDto, Pageable pageable) {
        Specification<Product> specification = new SpecificationBuilder(productFiltersDto).build();
        return productListToRepresentationDto(
                productRepository.findAll(specification, pageable));
    }

    private Page<ProductRepresentationDto> productListToRepresentationDto(Page<Product> products) {
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No product found.");
        }
        return products.map(this::mapToRepresentationDto);
    }

    public boolean existsByUpc(String upc) {
        return productRepository.existsByUpc(upc);
    }

    public Optional<Product> findByUpc(String productUpc) {
        return productRepository.findByUpc(productUpc);
    }

    public Product findProductOrThrow(String productUpc) {
        Optional<Product> product = findByUpc(productUpc);
        return product.orElseThrow(
                () -> new ResourceNotFoundException("Product not found."));
    }
    public void throwIfProductExists(String productUpc) {
        if (existsByUpc(productUpc)) {
            throw new ResourceAlreadyExistsException("Product with UPC " + productUpc + " already exists.");
        }
    }

    @Transactional
    public void delete(String upc) {
        productRepository.delete(findProductOrThrow(upc));
    }
    
    public ReturnProductDto updateProduct(String upc, CreateProductDto createProductDto){
        var productData = findProductOrThrow(upc);
        var productUpdated = mapToProduct(createProductDto);
        productUpdated.setId(productData.getId());
        productUpdated.setImageName(productData.getImageName());
        return mapToDto(
                save(
                        productUpdated));
    }

    public ReturnProductDto updateProductStatus(String upc, ProductStatusDto productStatusDto){
        var product = findProductOrThrow(upc);
        product.setIsActive(productStatusDto.getIsActive());
        return mapToDto(
                save(
                        product));
    }

    public ReturnProductDto updateProductCategories(String upc, CategoryIdDto categoryIdDto) {
        var product = findProductOrThrow(upc);
        product.setCategories(categoryService.getCategoriesById(categoryIdDto.getCategories()));
        return mapToDto(
                save(
                        product));
    }
    
    private ReturnProductDto mapToDto(Product product) {
        return modelMapper.map(product, ReturnProductDto.class);
    }

    private ProductRepresentationDto mapToRepresentationDto(Product product) {
        return modelMapper.map(product, ProductRepresentationDto.class);
    }

    private Product mapToProduct(CreateProductDto createProductDto) {
        return modelMapper.map(createProductDto, Product.class);
    }


}
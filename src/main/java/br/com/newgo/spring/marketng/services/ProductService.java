package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.ProductDtos.ProductDto;
import br.com.newgo.spring.marketng.dtos.ProductDtos.ProductStatusDto;
import br.com.newgo.spring.marketng.exceptions.ResourceAlreadyExistsException;
import br.com.newgo.spring.marketng.models.Product;
import br.com.newgo.spring.marketng.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import br.com.newgo.spring.marketng.exceptions.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    final ProductRepository productRepository;
    final ModelMapper modelMapper;
    final StorageService storageService;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, StorageService storageService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.storageService = storageService;
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    public ProductDto saveAndReturnDto(Product product) {
        return mapToDto(save(product));
    }

    public ProductDto saveProduct(ProductDto productDto) {
        throwIfProductExists(productDto.getUpc());
        return saveAndReturnDto(mapToProduct(productDto));
    }

    public ProductDto saveImage(MultipartFile image, String cup) throws IOException {
        var product = findProductOrThrow(cup);
        product.setImageName(storageService.storeFile(image));
        return saveAndReturnDto(product);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }
    public List<ProductDto> findAllAndReturnDto() {
        return productListToDtoOrThrowIfIsEmpty(findAll());
    }

    private List<ProductDto> productListToDtoOrThrowIfIsEmpty(List<Product> products){
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No product found.");
        }
        return products.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public ProductDto findProductAndReturnDto(UUID id) {
        return mapToDto(findProductOrThrow(id));
    }
    public ProductDto findProductAndReturnDto(String upc) {
        return mapToDto(findProductOrThrow(upc));
    }


    private Product findProductOrThrow(UUID id) {
        Optional<Product> product = findById(id);
        return product.orElseThrow(
                () -> new ResourceNotFoundException("Product not found."));
    }

    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }

    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<ProductDto> findByNameAndReturnDto(String name) {
        return productListToDtoOrThrowIfIsEmpty(findByNameContaining(name));
    }

    public List<Product> findByDescriptionContaining(String description) {
        return productRepository.findByDescriptionContainingIgnoreCase(description);
    }

    public List<ProductDto> findByDescriptionAndReturnDto(String description) {
        return productListToDtoOrThrowIfIsEmpty(findByDescriptionContaining(description));
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


    public ProductDto updateProduct(String cup, ProductDto productDto){
        var productData = findProductOrThrow(cup);
        var productUpdated = mapToProduct(productDto);
        productUpdated.setId(productData.getId());
        productUpdated.setImageName(productData.getImageName());
        return saveAndReturnDto(productUpdated);
    }

    public ProductDto updateProductStatus(String cup, ProductStatusDto productStatusDto){
        var product = findProductOrThrow(cup);
        product.setIsActive(productStatusDto.getIsActive());
        return saveAndReturnDto(product);
    }

    private ProductDto mapToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
    private Product mapToProduct(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

}
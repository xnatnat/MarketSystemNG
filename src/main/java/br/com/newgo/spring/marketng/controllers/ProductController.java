package br.com.newgo.spring.marketng.controllers;

import br.com.newgo.spring.marketng.dtos.ProductDto;
import br.com.newgo.spring.marketng.dtos.ProductStatusDto;
import br.com.newgo.spring.marketng.services.ProductService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@MultipartConfig
public class ProductController {
    final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductDto productDto,
                                       UriComponentsBuilder uriComponentsBuilder){
        var productData = productService.saveProduct(productDto);
        var uri = uriComponentsBuilder.path("/api/v1/products/{cup}").buildAndExpand(productData.getUpc()).toUri();
        return ResponseEntity.created(uri).body(productData);
    }

    @PostMapping("/image/{upc}")
    public ResponseEntity<ProductDto> saveProductImage(@PathVariable(value = "upc") String upc,
                                                                @RequestParam("image") MultipartFile image,
                                                                UriComponentsBuilder uriComponentsBuilder) throws IOException {
        return ResponseEntity.ok(productService.saveImage(image, upc));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(){
        return ResponseEntity.ok(productService.findAllAndReturnDto());
    }

    @GetMapping("/{upc}")
    public ResponseEntity<ProductDto> getByUpc(@PathVariable(value = "upc") String upc){
        return ResponseEntity.ok(productService.findProductAndReturnDto(upc));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDto>> getByName(@PathVariable(value = "name") String name){
        return ResponseEntity.ok(productService.findByNameAndReturnDto(name));
    }

    @GetMapping("/description/{description}")
    public ResponseEntity<List<ProductDto>> getByDescription(@PathVariable(value = "description") String description){
        return ResponseEntity.ok(productService.findByDescriptionAndReturnDto(description));
    }

    @DeleteMapping("/{upc}")
    public ResponseEntity<Void> delete(@PathVariable(value = "upc") String upc){
        productService.delete(upc);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = {"/{upc}"})
    public ResponseEntity<ProductDto> update(@PathVariable(value = "upc") String upc,
                                           @RequestBody @Valid ProductDto productDto){
        return ResponseEntity.ok(productService.updateProduct(upc, productDto));
    }

    @PutMapping(value = {"/image/{upc}"})
    public ResponseEntity<ProductDto> updateImage(@PathVariable(value = "upc") String upc,
                                              @RequestParam("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(productService.saveImage(image, upc));
    }

    @PutMapping({"/status/{upc}"})
    public ResponseEntity<ProductDto> updateStatus(@PathVariable(value = "upc") String upc,
                                                   @RequestBody @Valid ProductStatusDto productStatusDto){
        return ResponseEntity.ok(productService.updateProductStatus(upc, productStatusDto));
    }
}

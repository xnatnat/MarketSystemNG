package br.com.newgo.spring.marketng.controllers;

import br.com.newgo.spring.marketng.dtos.CategoryDtos.CategoryIdDto;
import br.com.newgo.spring.marketng.dtos.ProductDtos.*;
import br.com.newgo.spring.marketng.services.ProductService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<ReturnProductDto> save(@Valid @RequestBody CreateProductDto createProductDto,
                                                 UriComponentsBuilder uriComponentsBuilder){
        var productData = productService.requestSaveProduct(createProductDto);
        var uri = uriComponentsBuilder.path("/api/v1/products/{cup}").buildAndExpand(productData.getUpc()).toUri();
        return ResponseEntity.created(uri).body(productData);
    }

    @PostMapping("/image/{upc}")
    public ResponseEntity<ReturnProductDto> saveProductImage(@PathVariable(value = "upc") String upc,
                                                             @RequestParam("image") MultipartFile image,
                                                             UriComponentsBuilder uriComponentsBuilder) throws IOException {
        return ResponseEntity.ok(productService.saveImage(image, upc));
    }

    @GetMapping
    public ResponseEntity<Page<ReturnProductDto>> getAll(
            @PageableDefault(sort = "upc", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(productService.findAllAndReturnDto(pageable));
    }

    @GetMapping("/{upc}")
    public ResponseEntity<ReturnProductDto> getByUpc(@PathVariable(value = "upc") String upc){
        return ResponseEntity.ok(productService.findProductAndReturnDto(upc));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductRepresentationDto>> getByFilters(
            @PageableDefault(sort = "upc", direction = Sort.Direction.ASC) Pageable pageable,
            @ModelAttribute ProductFiltersDto productFiltersDto){
        return ResponseEntity.ok(productService.findByFiltersAndReturnDto(productFiltersDto, pageable));
    }

    @DeleteMapping("/{upc}")
    public ResponseEntity<Void> delete(@PathVariable(value = "upc") String upc){
        productService.delete(upc);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = {"/{upc}"})
    public ResponseEntity<ReturnProductDto> update(@PathVariable(value = "upc") String upc,
                                                   @RequestBody @Valid CreateProductDto createProductDto){
        return ResponseEntity.ok(productService.updateProduct(upc, createProductDto));
    }

    @PutMapping(value = {"/image/{upc}"})
    public ResponseEntity<ReturnProductDto> updateImage(@PathVariable(value = "upc") String upc,
                                                        @RequestParam("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(productService.saveImage(image, upc));
    }

    @PutMapping({"/status/{upc}"})
    public ResponseEntity<ReturnProductDto> updateStatus(@PathVariable(value = "upc") String upc,
                                                         @RequestBody @Valid ProductStatusDto productStatusDto){
        return ResponseEntity.ok(productService.updateProductStatus(upc, productStatusDto));
    }

    @PatchMapping({"/categories/{upc}"})
    public ResponseEntity<ReturnProductDto> updateCategories(@PathVariable(value = "upc") String upc,
                                                             @RequestBody CategoryIdDto categoryIdDto){
        return ResponseEntity.ok(productService.updateProductCategories(upc, categoryIdDto));
    }
}

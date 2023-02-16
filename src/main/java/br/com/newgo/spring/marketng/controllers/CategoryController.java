package br.com.newgo.spring.marketng.controllers;

import br.com.newgo.spring.marketng.dtos.CategoryDtos.CreateCategoryDto;
import br.com.newgo.spring.marketng.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CreateCategoryDto> save(@Valid @RequestBody CreateCategoryDto createCategoryDto,
                                                  UriComponentsBuilder uriComponentsBuilder){
        var categoryData = categoryService.requestSaveCategory(createCategoryDto);
        var uri = uriComponentsBuilder.path("/api/v1/categories/{id}").buildAndExpand(categoryData.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryData);
    }

    @GetMapping
    public ResponseEntity<List<CreateCategoryDto>> getAll(){
        return ResponseEntity.ok(categoryService.findAllAndReturnDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateCategoryDto> getById(@PathVariable(value = "id") UUID id){
        return ResponseEntity.ok(categoryService.findCategoryAndReturnDto(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateCategoryDto> update(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid CreateCategoryDto createCategoryDto){
        return ResponseEntity.ok(categoryService.updateCategory(id, createCategoryDto));
    }
}

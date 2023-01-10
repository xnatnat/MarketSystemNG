package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.CategoryDtos.CategoryDto;
import br.com.newgo.spring.marketng.exceptions.ResourceNotFoundException;
import br.com.newgo.spring.marketng.models.Category;
import br.com.newgo.spring.marketng.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    final CategoryRepository categoryRepository;
    final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public CategoryDto requestSaveCategory(CategoryDto categoryDto) {
        return mapToDto(
                save(
                        mapToCategory(
                                categoryDto)));
    }

    @Transactional
    public  Category save(Category category){
        return categoryRepository.save(category);
    }

    private Category mapToCategory(CategoryDto categoryDto){
        return modelMapper.map(categoryDto, Category.class);
    }

    private CategoryDto mapToDto(Category category){
        return modelMapper.map(category, CategoryDto.class);
    }

    public List<CategoryDto> findAllAndReturnDto() {
        return categoryListToDtoOrThrowIfIsEmpty(findAll());
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    private List<CategoryDto> categoryListToDtoOrThrowIfIsEmpty(List<Category> categories){
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("No product found.");
        }
        return categories.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public CategoryDto findCategoryAndReturnDto(UUID id) {
        return mapToDto(findCategoryOrThrow(id));
    }

    private Category findCategoryOrThrow(UUID id){
        Optional<Category> category = findById(id);
        return category.orElseThrow(
                () -> new ResourceNotFoundException("Category not found.")
        );
    }

    public Optional<Category> findById(UUID id){
        return categoryRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id) {
        categoryRepository.delete(findCategoryOrThrow(id));
    }

    public CategoryDto updateCategory(UUID id, CategoryDto categoryDto) {
        var category = findCategoryOrThrow(id);
        category.setName(categoryDto.getName());
        category.setDescription(category.getDescription());
        return mapToDto(save(category));
    }

    public Set<Category> getCategoriesById(Set<UUID> categoryIds) {
        return findAllByIdOrThrow(categoryIds)
                .stream()
                .filter(category -> categoryIds.contains(category.getId()))
                .collect(Collectors.toSet());
    }

    public List<Category> findAllByIdOrThrow(Set<UUID> categoryIds){
        var categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() < categoryIds.size())
            throw new ResourceNotFoundException("Some categories were not found.");
        return categories;
    }
}

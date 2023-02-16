package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.CategoryDtos.CreateCategoryDto;
import br.com.newgo.spring.marketng.models.Category;
import br.com.newgo.spring.marketng.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public CreateCategoryDto requestSaveCategory(CreateCategoryDto createCategoryDto) {
        return mapToDto(
                save(
                        mapToCategory(
                                createCategoryDto)));
    }

    @Transactional
    public  Category save(Category category){
        return categoryRepository.save(category);
    }

    private Category mapToCategory(CreateCategoryDto createCategoryDto){
        return modelMapper.map(createCategoryDto, Category.class);
    }

    private CreateCategoryDto mapToDto(Category category){
        return modelMapper.map(category, CreateCategoryDto.class);
    }

    public List<CreateCategoryDto> findAllAndReturnDto() {
        return categoryListToDtoOrThrowIfIsEmpty(findAll());
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    private List<CreateCategoryDto> categoryListToDtoOrThrowIfIsEmpty(List<Category> categories){
        throwIfCategoriesIsEmpty(categories);
        return categories.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private void throwIfCategoriesIsEmpty(List<Category> categories){
        if (categories.isEmpty())
            throw new EntityNotFoundException("No category found.");
    }

    public CreateCategoryDto findCategoryAndReturnDto(UUID id) {
        return mapToDto(findById(id).orElseThrow());
    }

    public Optional<Category> findById(UUID id){
        return categoryRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id) {
        categoryRepository.delete(findById(id).orElseThrow());
    }

    public CreateCategoryDto updateCategory(UUID id, CreateCategoryDto createCategoryDto) {
        var category = findById(id).orElseThrow();
        category.setName(createCategoryDto.getName());
        category.setDescription(createCategoryDto.getDescription());
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
            throw new EntityNotFoundException("Some categories were not found.");
        return categories;
    }
}

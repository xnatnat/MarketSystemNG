package br.com.newgo.spring.marketng.configs;

import br.com.newgo.spring.marketng.dtos.ProductDtos.CreateProductDto;
import br.com.newgo.spring.marketng.dtos.ProductDtos.ReturnProductDto;
import br.com.newgo.spring.marketng.models.Category;
import br.com.newgo.spring.marketng.models.Product;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

//        Converter<Set<Category>, Set<String>> categoryToString = context -> context
//                .getSource()
//                .stream()
//                .map(each -> each.getName())
//                .collect(Collectors.toSet());
//        modelMapper.typeMap(Product.class, ReturnProductDto.class).addMappings(src -> {
//            src.using(categoryToString);
//            src.map(Product::getCategories, ReturnProductDto::setCategoryName);
//        });
//        return modelMapper;

        Converter<Set<Category>, Set<String>> categoryToString = context -> {
            if (context.getSource() == null) {
                return null;
            }
            return context.getSource().stream()
                    .map(Category::getName)
                    .collect(Collectors.toSet());
        };
        modelMapper.typeMap(Product.class, ReturnProductDto.class).addMappings(src -> {
            src.using(categoryToString);
            src.map(Product::getCategories, ReturnProductDto::setCategoryName);
        });
        return modelMapper;
    }

}

package br.com.newgo.spring.marketng.dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProductRepresentationDto {
    @NotBlank
    @JsonProperty("nome")
    private String name;
    @JsonProperty("descricao")
    @NotBlank
    private String description;
    @JsonProperty("categorias")
    private Set<String> categoryName;
    @NotBlank
    private String brand;
    @JsonProperty("preco")
    @NotNull
    private Double price;

}

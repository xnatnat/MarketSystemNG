package br.com.newgo.spring.marketng.dtos.CategoryDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryDto {
    private UUID id;
    @NotBlank
    @JsonProperty("nome")
    private String name;
    @JsonProperty("descricao")
    @NotBlank
    private String description;

}

package br.com.newgo.spring.marketng.dtos.CategoryDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ReturnCategoryIdDto {
    @NotNull
    @JsonProperty("categorias")
    private Set<UUID> categories;
}

package br.com.newgo.spring.marketng.dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    @NotBlank
    @JsonProperty("nome")
    private String name;
    @JsonProperty("descricao")
    @NotBlank
    private String description;
    @JsonProperty("cup")
    @NotBlank
    @Size(max = 12)
    private String upc; //Universal Product Code
    @JsonProperty("marca")
    @NotBlank
    private String brand;
    @JsonProperty("quantidade")
    @NotNull
    private long quantity;
    @JsonProperty("preco")
    @NotNull
    private Double price;
    @JsonProperty("estaAtivo")
    @NotNull
    private Boolean isActive;
    @JsonProperty("imagemNome")
    private String imageName;
}
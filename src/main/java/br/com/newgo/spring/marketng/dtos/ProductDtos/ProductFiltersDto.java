package br.com.newgo.spring.marketng.dtos.ProductDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFiltersDto {

    @JsonProperty("nome")
    private String name;
    @JsonProperty("descricao")
    private String description;
    @JsonProperty("precoMinimo")
    private Double priceMin;
    @JsonProperty("precoMaximo")
    private Double priceMax;
    @JsonProperty("categoria")
    private String category;
}

package br.com.newgo.spring.marketng.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateProductListDto {
    private UUID id;
    @NotNull
    @JsonProperty("produtoCup")
    private String productUpc;
    @NotNull
    @JsonProperty("quantidade")
    private long quantity;
}

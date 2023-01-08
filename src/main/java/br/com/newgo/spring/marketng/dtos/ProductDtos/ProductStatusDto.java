package br.com.newgo.spring.marketng.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStatusDto {

    @JsonProperty("estaAtivo")
    @NotNull
    private Boolean isActive;

}

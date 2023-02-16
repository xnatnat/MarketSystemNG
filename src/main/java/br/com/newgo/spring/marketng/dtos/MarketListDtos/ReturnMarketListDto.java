package br.com.newgo.spring.marketng.dtos.MarketListDtos;

import br.com.newgo.spring.marketng.dtos.ProductListDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class MarketListWithIdDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID userId;
    @NotEmpty
    private Set<ProductListDto> products;
}

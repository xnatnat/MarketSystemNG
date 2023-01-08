package br.com.newgo.spring.marketng.controllers;

import br.com.newgo.spring.marketng.dtos.MarketListDtos.MarketListDto;
import br.com.newgo.spring.marketng.dtos.MarketListDtos.MarketListWithIdDto;
import br.com.newgo.spring.marketng.services.ProductListService;
import br.com.newgo.spring.marketng.services.ProductService;
import br.com.newgo.spring.marketng.services.MarketListService;
import br.com.newgo.spring.marketng.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/api/v1/MarketList")
public class MarketListController {
    final MarketListService marketListService;
    final UserService userService;
    final ProductService productService;
    final ProductListService productListService;
    public MarketListController(MarketListService marketListService,
                                UserService userService,
                                ProductService productService,
                                ProductListService productListService) {
        this.marketListService = marketListService;
        this.userService = userService;
        this.productService = productService;
        this.productListService = productListService;
    }

    @PostMapping
    public ResponseEntity<MarketListWithIdDto> save(@RequestBody @Valid MarketListDto marketListDto,
                                                    UriComponentsBuilder uriComponentsBuilder){
        var marketListData = marketListService.saveMarketList(marketListDto);
        var uri = uriComponentsBuilder.path("/api/v1/MarketList/{id}").buildAndExpand(marketListData.getId()).toUri();
        return ResponseEntity.created(uri).body(marketListData);
    }

    @GetMapping
    public ResponseEntity<List<MarketListWithIdDto>> getAll(){
        return ResponseEntity.ok(marketListService.findAllAndReturnDto());
    }
//TODO getByUserId
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") UUID id){
        return ResponseEntity.ok(marketListService.findMarketListAndReturnDto(id));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteById(@PathVariable(value = "id") UUID id){
        marketListService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id,
                                         @RequestBody @Valid MarketListDto marketListDto){
        return ResponseEntity.ok(marketListService.updateMarketListAndReturnDto(id, marketListDto));

    }
}

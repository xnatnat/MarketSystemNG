package br.com.newgo.spring.marketng.controllers;

import br.com.newgo.spring.marketng.dtos.MarketListDtos.CreateMarketListDto;
import br.com.newgo.spring.marketng.dtos.MarketListDtos.ReturnMarketListDto;
import br.com.newgo.spring.marketng.dtos.MarketListDtos.MarketListTotalDto;
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
    public ResponseEntity<ReturnMarketListDto> save(@RequestBody @Valid CreateMarketListDto createMarketListDto,
                                                    UriComponentsBuilder uriComponentsBuilder){
        var marketListData = marketListService.saveMarketList(createMarketListDto);
        var uri = uriComponentsBuilder.path("/api/v1/MarketList/{id}").buildAndExpand(marketListData.getId()).toUri();
        return ResponseEntity.created(uri).body(marketListData);
    }

    @GetMapping
    public ResponseEntity<List<ReturnMarketListDto>> getAll(){
        return ResponseEntity.ok(marketListService.findAllAndReturnDto());
    }
//TODO getByUserId
    @GetMapping("/{id}")
    public ResponseEntity<CreateMarketListDto> getById(@PathVariable(value = "id") UUID id){
        return ResponseEntity.ok(marketListService.findMarketListAndReturnDto(id));
    }

    @GetMapping("/total/{id}")
    public ResponseEntity<MarketListTotalDto> getTotalById(@PathVariable(value = "id") UUID id){
        return ResponseEntity.ok(marketListService.findTotalByIdAndReturnDto(id));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteById(@PathVariable(value = "id") UUID id){
        marketListService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<ReturnMarketListDto> update(@PathVariable(value = "id") UUID id,
                                                      @RequestBody @Valid CreateMarketListDto createMarketListDto){
        return ResponseEntity.ok(marketListService.updateMarketListAndReturnDto(id, createMarketListDto));
    }
}

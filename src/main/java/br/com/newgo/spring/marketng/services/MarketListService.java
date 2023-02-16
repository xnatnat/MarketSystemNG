package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.MarketListDtos.CreateMarketListDto;
import br.com.newgo.spring.marketng.dtos.MarketListDtos.MarketListTotalDto;
import br.com.newgo.spring.marketng.dtos.MarketListDtos.ReturnMarketListDto;
import br.com.newgo.spring.marketng.models.MarketList;
import br.com.newgo.spring.marketng.models.ProductList;
import br.com.newgo.spring.marketng.repositories.MarketListRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MarketListService {
    final MarketListRepository marketListRepository;
    final ModelMapper modelMapper;
    final UserService userService;
    private final ProductListService productListService;

    public MarketListService(MarketListRepository marketListRepository, ModelMapper modelMapper, UserService userService, ProductListService productListService) {
        this.marketListRepository = marketListRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.productListService = productListService;
    }

    @Transactional
    public MarketList save(MarketList marketList){
        return marketListRepository.save(marketList);
    }

    public ReturnMarketListDto saveAndReturnDtoWithId(MarketList marketList) {
        return mapToDto(save(marketList));
    }

    public ReturnMarketListDto saveMarketList(CreateMarketListDto createMarketListDto){
        MarketList marketList = createMarketList(createMarketListDto.getUserId());
        marketList.setProducts(
                productListService.saveProductListAndReturnProductList(
                        marketList, createMarketListDto.getProducts()));
        return saveAndReturnDtoWithId(
                calculateMarketListValue(marketList));
    }

    private MarketList createMarketList(UUID userId){
        return save(new MarketList(
                LocalDateTime.now(ZoneId.of("UTC")),
                null,
                userService.findById(userId).orElseThrow(),
                0.0));
    }

    private MarketList calculateMarketListValue(MarketList marketList){
        marketList.setTotal(
                marketList.getProducts()
                .stream()
                        .mapToDouble(ProductList::getTotal)
                        .sum()
        );
        return marketList;
    }

    public List<MarketList> findAll(){
        return marketListRepository.findAll();
    }

    public List<ReturnMarketListDto> findAllAndReturnDto() {
        return marketListToDtoOrThrowIfIsEmpty(findAll());
    }

    private List<ReturnMarketListDto> marketListToDtoOrThrowIfIsEmpty(List<MarketList> marketLists){
        if (marketLists.isEmpty()) {
            throw new EntityNotFoundException("No market list found.");
        }
        return marketLists.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public CreateMarketListDto findMarketListAndReturnDto(UUID id){
        return mapToDtoWithoutId(findById(id).orElseThrow());
    }

    public Optional<MarketList> findById(UUID id) {
        return marketListRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id){
        marketListRepository.delete(findById(id).orElseThrow());
    }


    public ReturnMarketListDto updateMarketListAndReturnDto(UUID id, CreateMarketListDto createMarketListDto) {
        userService.findById(createMarketListDto.getUserId()).orElseThrow();
        var marketListData = findById(id).orElseThrow();
        marketListData.setProducts(
                productListService.saveProductListAndReturnProductList(
                        marketListData, createMarketListDto.getProducts()));

        return saveAndReturnDtoWithId(calculateMarketListValue(marketListData));
    }

    public MarketListTotalDto findTotalByIdAndReturnDto(UUID id){
        return mapToTotalDto(findById(id).orElseThrow());
    }

    private MarketListTotalDto mapToTotalDto(MarketList marketList){
        return modelMapper.map(marketList, MarketListTotalDto.class);
    }

    private ReturnMarketListDto mapToDto(MarketList marketList) {
        return modelMapper.map(marketList, ReturnMarketListDto.class);
    }

    private CreateMarketListDto mapToDtoWithoutId(MarketList marketList) {
        return modelMapper.map(marketList, CreateMarketListDto.class);
    }
}


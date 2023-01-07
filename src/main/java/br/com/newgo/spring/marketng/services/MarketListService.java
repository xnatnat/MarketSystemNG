package br.com.newgo.spring.marketng.services;

import br.com.newgo.spring.marketng.dtos.MarketListDto;
import br.com.newgo.spring.marketng.dtos.MarketListWithIdDto;
import br.com.newgo.spring.marketng.exceptions.ResourceNotFoundException;
import br.com.newgo.spring.marketng.models.MarketList;
import br.com.newgo.spring.marketng.repositories.MarketListRepository;
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

    //TODO: todos os metodos que estão retornando DTO direto devem virar dois metodos, um que é chamado pelo controller e que salva e retorna dto e um que salva e retorna o objeto do banco
    public MarketListWithIdDto saveAndReturnDtoWithId(MarketList marketList) {
        return mapToDto(save(marketList));
    }

    public MarketListWithIdDto saveMarketList(MarketListDto marketListDto){
        var user = userService.findUserOrThrow(marketListDto.getUserId());
        var marketList = new MarketList();
        marketList.setUser(user);
        marketList.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        marketList = save(marketList);
        marketList.setProducts(
                productListService.saveProductListAndReturnProductList(
                        marketList, marketListDto.getProducts()));
        return saveAndReturnDtoWithId(marketList);
    }

    public List<MarketList> findAll(){
        return marketListRepository.findAll();
    }

    public List<MarketListWithIdDto> findAllAndReturnDto() {
        return marketListToDtoOrThrowIfIsEmpty(findAll());
    }

    private List<MarketListWithIdDto> marketListToDtoOrThrowIfIsEmpty(List<MarketList> marketLists){
        if (marketLists.isEmpty()) {
            throw new ResourceNotFoundException("No market list found.");
        }
        return marketLists.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public MarketListDto findMarketListAndReturnDto(UUID id){
        return mapToDtoWithoutId(findMarketListOrThrow(id));
    }

    private MarketList findMarketListOrThrow(UUID id) {
        Optional<MarketList> marketList = findById(id);
        return marketList.orElseThrow(
                () -> new ResourceNotFoundException("MarketList not found."));
    }

    public Optional<MarketList> findById(UUID id) {
        return marketListRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id){
        marketListRepository.delete(findMarketListOrThrow(id));
    }


    public MarketListWithIdDto updateMarketListAndReturnDto(UUID id, MarketListDto marketListDto) {
        userService.findUserOrThrow(marketListDto.getUserId());
        var marketListData = findMarketListOrThrow(id);
        marketListData.setProducts(
                productListService.saveProductListAndReturnProductList(
                        marketListData, marketListDto.getProducts()));

        return saveAndReturnDtoWithId(marketListData);
    }

    private MarketListWithIdDto mapToDto(MarketList marketList) {
        return modelMapper.map(marketList, MarketListWithIdDto.class);
    }

    private MarketListDto mapToDtoWithoutId(MarketList marketList) {
        return modelMapper.map(marketList, MarketListDto.class);
    }


    private MarketList mapToMarketList(MarketListDto marketListDto) {
        return modelMapper.map(marketListDto, MarketList.class);
    }

}


package br.com.newgo.spring.marketng.repositories;

import br.com.newgo.spring.marketng.models.MarketList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MarketListRepository extends JpaRepository<MarketList, UUID> {

}

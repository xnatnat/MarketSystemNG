package br.com.newgo.spring.marketng.repositories;

import br.com.newgo.spring.marketng.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}

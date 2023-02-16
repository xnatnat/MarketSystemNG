package br.com.newgo.spring.marketng.security;

import br.com.newgo.spring.marketng.models.User;
import br.com.newgo.spring.marketng.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
 *  Service responsável por manipular os tokens jwt
 *
 */
@Service
public class AuthenticationService {

    /* Tempo de expiração do token. Definido para 24h */
    static final long EXPIRATIONTIME = 864_000_00;
    /* Chave secreta da assinatura.
     Serve para verificar se a assinatura é válida (se o token não foi corrompido entre o tráfego cliente-servidor),
     e então permitir que os dados sejam extraídos da criptografia.
     valor dela será setado em application.properties
     */
    @Value("${api.security.jwt.signingkey}")
    private String SIGNINGKEY;
    /* Define o esquema de HTTP que será utilizado */
    static final String ISSUER = "API - MarketNG";

    final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Constroi o token jwt
     */
    public String generateToken(User user) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                    .sign(Algorithm.HMAC512(SIGNINGKEY));
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Erro ao gerar jwt token", exception);
        }
    }

    public Authentication getAuthentication(String jwtToken){
        try{
            return getAuthenticationByUser(
                    JWT.require(
                        Algorithm.HMAC512(SIGNINGKEY))
                        .withIssuer(ISSUER)
                        .build()
                        .verify(jwtToken)
                        .getSubject());
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token JWT invalido ou expirado");
        }
    }

    public Authentication getAuthenticationByUser(String email){
        var user = userRepository.findByEmail(email);
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

}





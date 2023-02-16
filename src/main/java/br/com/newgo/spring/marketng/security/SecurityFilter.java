package br.com.newgo.spring.marketng.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    final AuthenticationService authenticationService;

    public SecurityFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var tokenJwt = getTokenJwt(request);
        if(tokenJwt != null)
            SecurityContextHolder.getContext()
                                 .setAuthentication(
                                         authenticationService.getAuthentication(tokenJwt));
        filterChain.doFilter(request, response);
    }

    public String getTokenJwt(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        // acho que deve ser lançado um não autorizado e nao esse erro
        if(token != null){
            return token.replace("Bearer ", "");
        }
        return null;
    }
}

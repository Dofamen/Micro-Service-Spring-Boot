package org.sid.secservice.sec.filters;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // get the user that sent from the attemptAuthentication
        User user = (User) authResult.getPrincipal();
        // Choosing the Algorithm for Hash
        Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
        String jwtAccessToken = JWT.create()
                .withSubject(user.getUsername()) // userName of the user
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_ACCESS_TOKEN)) // date of expiration token
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);


        String jwtRefreshToken = JWT.create()
                .withSubject(user.getUsername()) // userName of the user
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 1000)) // date of expiration token
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String, String> idToken = new HashMap<>();

        idToken.put("access-token", jwtAccessToken);
        idToken.put("refresh=token", jwtRefreshToken);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }
}
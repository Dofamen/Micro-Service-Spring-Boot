package org.sid.secservice.sec.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.sid.secservice.sec.entities.AppRole;
import org.sid.secservice.sec.entities.AppUser;
import org.sid.secservice.sec.filters.JWTUtil;
import org.sid.secservice.sec.service.AccountService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AccountRestController {

    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/users")
    @PostAuthorize("hasAuthority('USER')")
    public List<AppUser> appUsers() {
        return accountService.listUsers();
    }

    @PostMapping("/users")
    public AppUser saveUser(@RequestBody AppUser appUser) {
        return accountService.addNewUser(appUser);
    }

    @PostMapping("/role")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addNewRole(appRole);
    }

    @PostMapping("/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) throws Exception {
        accountService.addRoleToUser(roleUserForm.username, roleUserForm.getRolename());
    }

    @GetMapping(path = "/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authToken = request.getHeader(JWTUtil.AUTH_HEADER);
        if (authToken != null && authToken.startsWith(JWTUtil.PREFIX)) {
            try {
                String jwt = authToken.substring(JWTUtil.PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256("MySecretKey123");

                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();
                AppUser user = accountService.loadUserByUserName(username);

                String jwtAccessToken = JWT.create()
                        .withSubject(user.getUserName()) // userName of the user
                        .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // date of expiration token
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getAppRoles().stream().map(AppRole::getRoleName).collect(Collectors.toList()))
                        .sign(algorithm);

                String jwtRefreshToken = JWT.create()
                        .withSubject(user.getUserName()) // userName of the user
                        .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_REFRESH_TOKEN)) // date of expiration token
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);
                Map<String, String> idToken = new HashMap<>();

                idToken.put("access-token", jwtAccessToken);
                idToken.put("refresh=token", jwtRefreshToken);

                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), idToken);
            } catch (Exception q) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

        } else {
            throw new RuntimeException("Le Refresh Token Obligatoire");
        }
    }

    @GetMapping("/profile")
    public AppUser profile(Principal principal){
            return accountService.loadUserByUserName(principal.getName());
    }
}

@Data
class RoleUserForm {
    String username;
    String rolename;
}

package ma.enset.supplierservice.sec;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KCAdapterConfig {

    @Bean
    public KeycloakSpringBootConfigResolver springBootConfigResolver(){
        // lire la configuration dans application.properties
        return new KeycloakSpringBootConfigResolver();
    }
}

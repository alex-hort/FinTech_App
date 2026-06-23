import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // Configura y registra el filtro CORS para toda la aplicación.
    // CORS controla qué dominios externos pueden hacer requests a esta API.
    @Bean
    public CorsFilter corsFilter() {

        // Define a qué rutas aplica esta configuración CORS.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permite requests desde cualquier origen (dominio).
        // En producción conviene reemplazar "*" por el dominio del frontend, ej: "https://mifrontend.com"
        config.addAllowedOrigin("*");

        // Permite cualquier header en el request (Authorization, Content-Type, etc.).
        config.addAllowedHeader("*");

        // Permite cualquier método HTTP (GET, POST, PUT, DELETE, etc.).
        config.addAllowedMethod("*");

        // El navegador puede cachear esta configuración CORS por 3600 segundos (1 hora)
        // sin necesidad de hacer un preflight request cada vez.
        config.setMaxAge(3600L);

        // Aplica esta configuración a todas las rutas de la API.
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
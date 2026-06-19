import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;

// Indica que esta clase contiene configuraciones de Spring
@Configuration
public class AppConfig {
    // Registra un objeto (Bean) en el contenedor de Spring
    @Bean
    public SpringTemplateEngine templateEngine() {
        // Motor de Thymeleaf encargado de procesar las plantillas HTML
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Busca las plantillas dentro de los recursos del proyecto
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        // Carpeta donde se encuentran los archivos HTML
        templateResolver.setPrefix("templates/");
        // Extensión de los archivos de plantilla
        templateResolver.setSuffix(".html");
        // Codificación para soportar caracteres especiales y acentos
        templateResolver.setCharacterEncoding("UTF-8");
        // Asocia el buscador de plantillas al motor de Thymeleaf
        templateEngine.setTemplateResolver(templateResolver);
        // Devuelve el motor configurado para que Spring lo utilice
        return templateEngine;
    }

    @Bean
    public ModelMapper modelMapperConfig() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
        .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
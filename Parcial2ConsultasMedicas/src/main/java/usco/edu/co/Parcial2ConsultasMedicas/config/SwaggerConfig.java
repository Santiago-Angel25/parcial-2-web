package usco.edu.co.Parcial2ConsultasMedicas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI consultasMedicasOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Centro de Gestion de Consultas Medicas")
                        .version("1.0")
                        .description("Servicios web para administrar consultas medicas con seguridad por roles.").
                termsOfService("http://algo.com/terminos")
                .contact(new io.swagger.v3.oas.models.info.Contact().name("Diego Carvajal")
                        .email("diego.carvajal@usco.edu.co").url("http://algo.com"))
                .license(new io.swagger.v3.oas.models.info.License().name("Licencia MIT")
                        .url("http://opensource.org/licenses/MIT")));
    }
}

package _bbu.lawfirmapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(info = @Info(title = "LAW FIRM Rest API", version = "v1", description = "Welcome to our law firm API. With this API, you can test it with your Law firm web application. "), servers = {
        @Server(url = "/", description = "Default Server URL") })
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", in = SecuritySchemeIn.HEADER)
@SpringBootApplication
@EntityScan(basePackages = "_bbu.lawfirmapi.models.Entity")
@EnableJpaRepositories(basePackages = "_bbu.lawfirmapi.repositories")
public class LawfirmApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawfirmApiApplication.class, args);
    }

}

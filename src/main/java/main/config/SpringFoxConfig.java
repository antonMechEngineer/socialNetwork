package main.config;

import com.fasterxml.classmate.TypeResolver;
import main.api.response.CommonResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.ArrayList;

@Configuration
public class SpringFoxConfig {

    private final TypeResolver typeResolver;

    public SpringFoxConfig(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .additionalModels(typeResolver.resolve(CommonResponse.class));
    }

    public ApiInfo apiInfo() {
        return new ApiInfo(
                "Zerone API",
                "API for social network",
                "1.0",
                "",
                new Contact("", "", ""),
                "",
                "",
                new ArrayList<>()
        );
    }

    @Bean
    public UiConfiguration tryItOutConfig() {
        final String[] methodsWithTryItOutButton = {};
        return UiConfigurationBuilder.builder().supportedSubmitMethods(methodsWithTryItOutButton).build();
    }
}

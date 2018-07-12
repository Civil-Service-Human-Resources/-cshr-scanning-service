package uk.gov.cshr.scanner.swagger;

import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration to enable and setup Swagger
 */
@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {
    @Value("${base.pacakge}")
    private String basePackage;

    @Bean
    public Docket vacancyApi() {
        log.info("BASE PACKAGE = " + basePackage);

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .enableUrlTemplating(false)
                .tags(new Tag("CSHR Scanning Service",
                                "API relating to the scanning of a given file for viruses and malware"));
    }
}

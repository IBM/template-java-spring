package com.ibm.cloud_garage.swagger;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component
@EnableSwagger2
public class SwaggerDocket {
    @Autowired
    private SwaggerConfig config;

    public SwaggerDocket() {
        super();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(buildApiRequestHandler())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(buildApiInfo());
    }

    protected Predicate<RequestHandler> buildApiRequestHandler() {
        if (!StringUtils.isEmpty(config.getBaseApiPackage())) {
            return buildBasePackageRequestHandler(config.getBaseApiPackage());
        }

        return RequestHandlerSelectors.any();
    }

    protected Predicate<RequestHandler> buildBasePackageRequestHandler(final String baseApiPackage) {
        return RequestHandlerSelectors.basePackage(baseApiPackage);
    }

    protected ApiInfo buildApiInfo() {
        return new ApiInfo(
                config.getTitle(),
                config.getDescription(),
                config.getVersion(),
                config.getTermsOfServiceUrl(),
                buildContact(),
                config.getLicense(),
                config.getLicenseUrl(),
                config.getVendorExtensions());
    }

    protected Contact buildContact() {
        final SwaggerConfig.Contact contact = config.getContact();

        return contact != null ? new Contact(contact.getName(), contact.getUrl(), contact.getEmail()) : null;
    }
}

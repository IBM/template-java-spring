package com.ibm.cloud_native_toolkit.swagger;

import com.ibm.cloud_native_toolkit.util.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.function.Predicate;
import java.util.Collection;

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
                .apis(buildApiRequestHandler()::test)
                .paths(buildPathSelector()::test)
                .build()
                .apiInfo(buildApiInfo());
    }

    protected Predicate<RequestHandler> buildApiRequestHandler() {
        if (!CollectionUtils.isEmpty(config.getBaseApiPackages())) {
            System.out.println("Building api request handler from base packages: " + config.getBaseApiPackages().toString());
            return buildBasePackageRequestHandler(config.getBaseApiPackages());
        }

        System.out.println("Building api request handler from base packages: any");
        return Predicates.any();
    }

    protected Predicate<RequestHandler> buildBasePackageRequestHandler(final Collection<String> baseApiPackages) {
        return baseApiPackages
            .stream()
            .reduce(
                null,
                (Predicate<RequestHandler> handler, String baseApiPackage) -> {
                    final Predicate<RequestHandler> predicate = RequestHandlerSelectors.basePackage(baseApiPackage)::apply;

                    if (handler == null) {
                        return predicate;
                    } else {
                        return handler.or(predicate);
                    }
                },
                (handler, predicate) -> {
                    if (handler == null) {
                        return predicate;
                    } else {
                        return handler.or(predicate);
                    }
                });
    }

    protected Predicate<String> buildPathSelector() {
        if (!CollectionUtils.isEmpty(config.getPathRegEx())) {
            return buildPathSelectorHandler(config.getPathRegEx());
        }

        return Predicates.any();
    }

    protected java.util.function.Predicate<String> buildPathSelectorHandler(final Collection<String> pathRegExValues) {
        return pathRegExValues
            .stream()
            .reduce(
                null,
                (Predicate<String> handler, String pathRegEx) -> {
                    final Predicate<String> predicate = (String input) -> input.matches(pathRegEx);

                    if (handler == null) {
                        return predicate;
                    } else {
                        return handler.or(predicate);
                    }
                },
                (handler, predicate) -> {
                    if (handler == null) {
                        return predicate;
                    } else {
                        return handler.or(predicate);
                    }
                });
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

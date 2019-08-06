package com.ibm.hello.controller;

import static com.ibm.hello.service.ServiceNameConstants.HELLO_NAME;
import static com.ibm.hello.service.ServiceNameConstants.HOLA_NAME;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.hello.config.ServiceConfig;
import com.ibm.hello.model.GreetingRequest;
import com.ibm.hello.model.GreetingResponse;
import com.ibm.hello.service.GreetingService;
import com.ibm.hello.service.ServiceName;

@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    private final BeanFactory beanFactory;
    private final ServiceConfig serviceConfig;

    public HelloController(BeanFactory beanFactory, ServiceConfig serviceConfig) {
        this.beanFactory = beanFactory;
        this.serviceConfig = serviceConfig;
    }

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Name parameter missing")
    })
    public ResponseEntity<GreetingResponse> helloWorld(
            @RequestParam(name = "name", required = false) final String name,
            @ApiParam(
                    allowableValues = HELLO_NAME + "," + HOLA_NAME,
                    value = "the beanName for the service implementation that should be used to fulfill the request")
            @RequestHeader(name = "serviceName", required = false) final String serviceName
    ) {

        LOGGER.debug("Processing name: " + name);

        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.status(406).build();
        }

        return ResponseEntity.ok(
                getGreetingService(serviceName)
                        .getGreeting(name));
    }

    @PostMapping(
            value = "/hello",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Content body is missing"),
            @ApiResponse(code = 406, message = "Name parameter missing"),
            @ApiResponse(code = 415, message = "Missing content type")
    })
    public ResponseEntity<GreetingResponse> helloWorld(
            @RequestBody GreetingRequest request,
            @ApiParam(
                    allowableValues = HELLO_NAME + "," + HOLA_NAME,
                    value = "the beanName for the service implementation that should be used to fulfill the request")
            @RequestHeader(name = "serviceName", required = false) final String serviceName
    ) {

        LOGGER.debug("Processing name: " + request.getName());

        if (StringUtils.isEmpty(request.getName())) {
            return ResponseEntity.status(406).build();
        }

        return ResponseEntity.ok(getGreetingService(serviceName).getGreeting(request.getName()));
    }

    protected GreetingService getGreetingService(String serviceNameHeader) {
        final ServiceName serviceName = ServiceName.safeValueOf(
                serviceNameHeader,
                serviceConfig.getBeanName());

        if (serviceName == null) {
            throw new ApplicationConfigurationError("ServiceConfig.beanName from ");
        }

        return beanFactory.getBean(serviceName.simpleName(), GreetingService.class);
    }

    public static class ApplicationConfigurationError extends RuntimeException {
        public ApplicationConfigurationError(String message) {
            super(message);
        }
    }
}

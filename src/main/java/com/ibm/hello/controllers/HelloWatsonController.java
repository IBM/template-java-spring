package com.ibm.hello.controllers;

import com.ibm.hello.models.SimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
public class HelloWatsonController {

    @GetMapping(value = "/hello", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Say hello to Watson")
    public SimpleResponse<String> hello() {
        return new SimpleResponse<>("Hello, Watson!");
    }

    @GetMapping(value = "/hello/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(
            summary = "Say hello to someone",
            parameters = {
                    @Parameter(
                            name = "name",
                            description = "Name of person to greet",
                            extensions = @Extension(properties = {@ExtensionProperty(name = "x-ibm-label", value = "Name")})
                    )
            }
    )
    public SimpleResponse<String> helloName(@PathVariable("name") String name) {
        return new SimpleResponse<>(String.format("Hello, %s!", name));
    }
}

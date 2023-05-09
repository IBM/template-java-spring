<p align="center">
    <a href="https://cloud.ibm.com">
        <img src="https://landscape.cncf.io/logos/ibm-cloud-kcsp.svg" height="100" alt="IBM Cloud">
    </a>
</p>

<p align="center">
    <a href="https://cloud.ibm.com">
    <img src="https://img.shields.io/badge/IBM%20Cloud-powered-blue.svg" alt="IBM Cloud">
    </a>
    <a href="https://www.ibm.com/developerworks/learn/java/">
    <img src="https://img.shields.io/badge/platform-java-lightgrey.svg?style=flat" alt="platform">
    </a>
    <img src="https://img.shields.io/badge/license-Apache2-blue.svg?style=flat" alt="Apache 2">
</p>


# Java Spring microservice starter kit

This repo provides a starting point for creating Java microservice applications running on [Spring](https://spring.io/).
It contains a simple Hello World application as an example of how to get started, as well as a number of standard features.

The port defaults to `8080` and can be updated in `application.yaml` or overridden as environment variables.

## Getting started

After creating a repository from the template, make the following updates:

1. Change the application name in "settings.gradle"
2. Update the `NAME`, `SUMMARY`, `DESCRIPTION`, and `VENDOR` default values in "Dockerfile"
3. Update the `name` value for the chart in "chart/hello-watson/Chart.yaml"
4. Change the directory name of the helm chart to match the value in "chart/hello-watson/Chart.yaml"

## Features

### Health endpoint

A simple health endpoint is provided for use in the readiness and liveness probes. The
endpoint simply returns `{"response": "OK"}` when called. As long as the Spring server
is running, this endpoint should work.

### OpenApi 3.0 config

The SpringDoc module has been added to give support for OpenApi 3 configuration. This is
exposed in a couple of ways:

- Swagger UI - the swagger ui can be access from `/swagger-ui.html` to allow access to the api
- OpenApi spec - the JSON OpenApi spec can be accessed from `/v3/api-docs` and the yaml version from `/v3/api-docs.yaml`

The configuration for the OpenApi spec comes from a combination of the `OpenApiConfiguration` object and
annotations provided on the individual endpoints. Any endpoint or RestController can be excluded from the
api spec generation by adding the `@Hidden` annotation.

### Helm chart

A helm chart is provided to deploy the application to a kubernetes server. The helm
chart is provided in the `chart/` directory.

### Dockerfile (springboot fat jar)

The Dockerfile file contains a multistage container build process to package the fat spring jar in a builder
stage and bundle the jar into an executable java runtime image. Both the builder and runtime images are based
on UBI9 images.

## Configuration

The following configuration values are available for development-time configuration of default values (`application.yaml`)
and/or runtime configuration values that can be provided as environment variables.

| Description                | application.yaml    | Env var         |
|----------------------------|---------------------|-----------------|
| Port the server listens on | server.port         | PORT            |
| Id of the api              | openapi.id          | API_ID          |
| Title of the api           | openapi.title       | API_TITLE       |
| Description of the api     | openapi.description | API_DESCRIPTION |


## Development

### Open Api documentation

There are a number of ways to customize the Open API spec that is generated from the application code.

#### Spec configuration

The configuration for the general elements of the Open API spec are provided in the `OpenApiConfiguration` object.
Values for the spec are pulled from the `application.yaml` file and can be overridden by environment variables at
deployment time. Currently, the configuration provided in the OpenApiConfiguration covers the basic values. It can be extended and
customized for specific requirements.

The Open API configuration is provided in a Java object with a number of attributes. Many of the values will be generated
from the annotations on the endpoints and entities.

#### Endpoint annotations

An individual endpoint methods within the RestController can be annotated using the `@Operation` annotation. For example,
adding a summary, documenting parameters, and adding extensions would look like the following:

```java
@RestController
public class HelloWatsonController {
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
```

The operation annotation supports a number of attributes. See [Operation docs](https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/io/swagger/v3/oas/annotations/Operation.html) for more details.

#### Entity annotations

The API documentation for entity schemas can be manipulated using the `@Schema` annotation
on individual fields. For example, adding a description and extension to a property of an entity would look like the following:

```java
public class MyEntity {
    @Schema(
            description = "Employee ID",
            extensions = @Extension(properties = {
                    @ExtensionProperty(name = "x-ibm-disable", value = "true")
            })
    )
    private String employeeId;
}
```

The schema annotation supports a number of attributes. See [Schema docs](https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/io/swagger/v3/oas/annotations/media/Schema.html) for more details.

### Gradle

Gradle has been selected as the build tool. The Gradle dependencies are included in the repository and can be
access via the `gradlew`/`gradlew.bat` script which will work so long as Java is available. Here are some basic
commands for development:

- `./gradlew clean` - clean previous build outputs
- `./gradlew build` - compile Java source
- `./gradlew test` - execute unit tests
- `./gradlew package` - build the jar/war package file
- `./gradlew bootRun` - run the executable application components in a hot-swappable development environment

### OCI

A Dockerfile is provided to build a container image for running the application. The container image definition
contains default ARG values used for labels in the built container image. The values can be updated in the Dockerfile
and/or provided at build time by using the `--build-arg` flag.
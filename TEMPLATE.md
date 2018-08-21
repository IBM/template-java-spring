# Template Overview

## Files

### Template files

The following files are provided by the template. These files should not be modified so they can
be more easily updated from the template in the future. The mechanisms to customize the 
configuration or behavior of these files are provided below in the [Customizing behavior](#customizing) section.

* `gradle/build-xxx.gradle` - build scripts
* `config/checkstyle/checkstyle.xml` - checkstyle config
* `config/githooks/*` - githook config
* `setup-template.sh` - script to initially create a project from the template
* `update-template.sh` - script to periodically refresh the template content from the template repo
* `TEMPLATE.md` - template documentation
* `src/*/java/com/ibm/cloud_garage/swagger/*` - code and test files to activate and configure Swagger

### Template usage example files

The template also includes a simple sample Spring Boot application that can be used as a reference 
when getting started and deleted whenever they are no longer needed. The sample code is in the 
`com.ibm.hello` package root with sub-packages for the various application components. It is 
recommended that any Spring Boot application follow a similar structure:

* `app.Application` - Spring Boot Application entry point 
* `controller.HelloController` - REST controller entry point
* `service.HelloService` - Service logic 
* `config.HelloConfig` - configuration model

The sample application uses Spring dependency injection and loads the configuration from the
application.yml file into the config object. The sample application can be run with 
`./gradlew bootRun`. The url for the Swagger page will be `http://localhost:9080/swagger-ui.html`

### Project files

Initial versions of the following files are provided as part of the template and can be customized
for the project as appropriate.

* `build.gradle` - main gradle build file 
* `settings.gradle` - gradle settings (primarily for the rootProject.name property to start)
* `resources/application.yml` - application config 
* `resources/logback-spring.xml` - logging config
* `README.md` - project documentation

Since the template contains versions of these files, running the update script *might* trigger a 
merge conflict on these files. Those template file changes for these files can either be ignored
completely or merged in with the changes made by the project for those files.

## Features

Most all of the features are included in the template through applied build script files 
(incorporated via the `apply from ...` lines in `build.gradle`). They are enabled by default but
can be turned off by commenting the `apply from ...` line out. The behavior of these features can
also be customized as described in the in the [Customizing behavior](#customizing) section below. 
Some features also require additional supporting files.

### Linting (`checkstyle`)

Linting rules are applied via the `gradle/build-checkstyle.gradle` build script. It also relies on 
the provided checkstyle config in `config/checkstyle/checkstyle.xml`

### Git hooks

Git hooks are scripts that can be added to the project to run before or after certain commands. 
A pre-commit hook is currently provided to stash uncommitted changes, run `./gradlew check`, and pop
the stashed changes when done every time `git commit` is run. The pre-commit hook can be skipped on
a case-by-case basis by adding the `--no-verify` flag.

The git hook is added via the `gradle/build-githooks.gradle` build script. It uses supporting git
hook script files in `config/githooks`.

### Code coverage (`jacoco`)

Code coverage reporting and verification is provided using the `jacoco` library via the 
`gradle/build-jacoco.gradle` build script. Configuration can be provided to the build script by
adding the following to the `build.gradle` file:

```groovy
ext {
  config = [
    jacoco: [
      limits: [
        'bundle': 0.2,
        'clazz': 0.0
      ],
      excludes: ['**/Application.class']
    ]
  ]
}
```

The `limits.bundle` and `limits.clazz` values set the covered ratio level for code coverage verification. 
The `excludes` value provides a list of patterns that should be applied to exclude classes from reporting 
and verification.

The coverage report is created in `build/reports/jacoco/test/html`.

### Unit testing (`junit5`)

Support for Junit 5 is provided via `gradle/build-junit5.gradle`. Additionally, JUnit 4 libraries
and the vintage runner have been configured.

### Health reporting (`springactuator`)

Spring provides a facility to report on the health and configuration of the microservice by way
of a library called `springactuator`. The library is loaded via `gradle/build-springactuator.gradle`.

### Springboot

Spring boot configuration is provided via `gradle/build-springboot.gradle`.

### API documentation (`swagger`)

Swagger is a library that generates documentation for REST APIs. The Spring Boot annotations will generate
good documentation out of the box. Additional Swagger annotations can be added to the REST code to enhance
the documentation. The swagger component is activated via the `gradle/build-swagger.gradle` build script
and the code components in `com.ibm.cloud_garage.swagger`. Configuration for the title, description, and
other values are defined in the `resources/application.yml` file under the `swagger` section.

### Test reporting

The junit5 console reporting lacks some detail which is provided from the `com.adarshr.test-logger` plugin
via the `gradle/build-testlogger.gradle`. By default it does 'mocha' style reporting but that can be
configured by adding the following to the `build.gradle` file:

```groovy
ext {
  config = [
    testLogger: [
      theme: 'mocha'
    ]
  ]
}
```

### jar

To output a jar file, the `gradle/build-jar.gradle` build script can be included.

### war

To output a war file, the `gradle/build-jar.gradle` build script can be included. The default configuration 
of `build.gradle` is to produce a war file because that is the recommended way to deploy to liberty.

### Pact testing

The project adds support for consumer- and provider-side Pact testing via three gradle build files:

* gradle/build-pact.gradle
* gradle/build-pact-consumer.gradle
* gradle/build-pact-provider.gradle

To enable Pact testing support, simply add `apply from: 'build-pact-consumer.gradle'` and/or `apply from: 'build-pact-provider.gradle'` to
`build.gradle` depending on the nature of the application. (The `gradle/build-pact.gradle` file is referenced within the other two files.)

On the provider side, if you are not using a pact-broker then the configuration can become rather custom. In that case it
is recommended to copy the existing build-pact-provider.gradle to another file and make approriate customizations.

For more information on Pact testing see [https://docs.pact.io/](https://docs.pact.io/)

### Spring tools

#### Intellij setup

1. Open the `Settings --> Build-Execution-Deployment --> Compiler` and enable the Make Project Automatically.

2. Then press `ctrl+shift+A` and search for the registry. In the registry, make the following configuration enabled:
 
   `compiler.automake.allow.when.app.running`
   
3. Restart the IDE.


## Usage

The following sections provide common actions to perform on the project.

### Clean the build environment

`./gradlew clean`

### Run the unit tests

`./gradlew test`

To run continuously and watch for changes:

`./gradlew test --continuous`

### Run the lint and code coverage tests

`./gradlew check`

To run continuously and watch for changes:

`./gradlew check --continuous`

### Generate the application archive

`./gradlew assemble`

The resulting binaries will be placed in `build/libs`. If the `build-war.gradle` script is used, then a 
war file will be generated. If the `build-jar.gradle` script is used, then a jar file will be generated.

### Start the server (Liberty)

To run the application on Liberty, run the following:

`./gradlew libertyRun`

### Start the server (embedded Tomcat)

To run the embedded server configured for Spring Boot, run the following:

`./gradlew bootRun`

### View the Swagger documentation

With the server running, the Swagger documentation can be accessed at the following:

`http://localhost:9080/swagger-ui.html`

### View the Spring Actuator information

With the server running, the Spring Actuator information can be accessed at the following:

`http://localhost:9080/metrics`

That URL will give the index to the other service urls.

### Skip the pre-commit hook

The pre-commit hook can be bypassed with the following:

`git commit --no-verify`

### Configure the IBM Cloud Continuous Delivery toolchain and pipeline

Run the script to update the toolchain config using the following:

`./setup-bluemix-toolchain.sh`

Once the toolchain has been updated, commit and push the changes then click the `Create toolchain` button
in the `PIPELINE.md` file.  

## <a name="customizing"></a>Customizing behavior

### Code coverage configuration

The code coverage settings can be configured by providing the following information in the `build.gradle` file:

```groovy
ext {
  config = [
    jacoco: [
      limits: [
        'bundle': 0.2,
        'clazz': 0.0
      ],
      excludes: ['**/Application.class']
    ]
  ]
}
```

### Checkstyle configuration

By default the checkstyle plugin points to the config file located at `config/checkstyle/checkstyle.xml`. To change
the config, provide a different checkstyle configuration file and add something like the following to the
`build.gradle` file:

```groovy
checkstyle {
    configFile ${configDir}/checkstyle-custom.xml
}
```

### Pact testing

Pact testing is consumer-driven contract testing, so it makes sense to start with the consumer.

#### Consumer

1. Add `apply from: 'gradle/build-pact-consumer.gradle'` to the `build.gradle` file.
2. Create the Pact test to define the contract. An example consumer can be found in the 
[seansund/hello-world-consumer](https://github.ibm.com/seansund/hello-world-consumer) repo.
That consumer project was built using this template and does Pact testing for the HelloWorld
service provided as the sample application for this repo.
3. Test the pact on the consumer by running `./gradlew test`. This will validate the pact and 
generate the pact file that should be used to validate the provider.
4. (Optionally) The pact can be published to a pact-broker by running `./gradlew pactPublish`. 
By default the build assumes the pact-broker is running at `http://localhost`. To override that,
run the pactPublish command with `./gradlew pactPublish -PpactBrokerUrl={url}`
5. (Optionally) The consumer pact can be validated against the swagger documentation of the 
provider by running `yarn pact:test {swaggerUrl}` where {swaggerUrl} is the url to the swagger
json file. If not provided the value defaults to `http://localhost:9080/v2/api-docs` which is the
default url where Swagger exposes the json configuration file.

#### Provider

1. Add `apply from: 'gradle/build-pact-provider.gradle'` to the `build.gradle` file.
2. Get the pact file from the consumer. By default, the build assumes that a pact broker will be 
used and that the pact broker is running on `http://localhost`. If necessary the pact file can be
manually copied to a directory before running but this is not recommended. To connect to a pact
broker running other than `localhost` then pass `-PpactBrokerUrl={url}` to the verify command.
3. Test the pact on the consumer by running `./gradlew pactVerify`. This will build the app, 
install it to the Liberty server, start the Liberty server, validate the API using the pact 
file provided against the running server, then stop the Liberty server. (Note: publishing the results
back to the pact broker seems to be broken in the Junit5 implementation)

#### Pact-Broker

A pact broker provides a central place for consumers to publish pacts and providers to access
those pacts for verification. A docker image exists that requires some configuration. To speed up the
process of getting a pact broker up and running, configuration and scripts have been provided in
[seansund/pact-broker-kube](https://github.ibm.com/seansund/pact-broker-kube). The repo contains
scripts to run in docker image(s) or in an IBM Cloud Kubernetes container.

## Upcoming features

* TraceId and SpanId for logging context
* Log incoming and outgoing request and response
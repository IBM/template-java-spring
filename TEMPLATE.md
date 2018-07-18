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

### Start the server

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

## Upcoming features

* Liberty embedded server
* DevOps scripts
* TraceId and SpanId for logging context
* Log incoming and outgoing request and response
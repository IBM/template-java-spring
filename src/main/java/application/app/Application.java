package application.app;

import io.jaegertracing.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@ComponentScan({"com.ibm.cloud_native_toolkit.*", "com.ibm.health", "com.ibm.hello"})
public class Application extends SpringBootServletInitializer {
    @Autowired
    Environment environment;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println();
            System.out.println("Server started - http://localhost:" + this.port + "/swagger-ui.html");
        };
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public io.opentracing.Tracer initTracer() {
        printEnv();

        final String serviceName = !StringUtils.isEmpty(this.applicationName) ? this.applicationName : "template-spring-boot";
        System.out.println("Service name (" + this.applicationName + "): " + serviceName);

        Configuration.SamplerConfiguration samplerConfig = samplerConfiguration();
        Configuration.ReporterConfiguration reporterConfig = reporterConfiguration();

        try {
            return Configuration
                .fromEnv(serviceName)
                .withSampler(samplerConfig)
                .withReporter(reporterConfig)
                .getTracer();
        } catch (RuntimeException e) {
            System.err.println("Error creating tracer");
            e.printStackTrace();
            throw e;
        }
    }

    protected void printEnv() {
        final Map<String, String> env = System.getenv();

        final String envValues = env.entrySet()
            .parallelStream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .sorted()
            .collect(Collectors.joining("\n"));

        System.out.println("Environment variables: ");
        System.out.println(envValues);
    }

    protected Configuration.SamplerConfiguration samplerConfiguration() {
        try {
            return new Configuration.SamplerConfiguration()
                .withType("const")
                .withParam(1);
        } catch (RuntimeException e) {
            System.err.println("Error creating sampler configuration");
            e.printStackTrace();
            throw e;
        }
    }

    protected Configuration.ReporterConfiguration reporterConfiguration() {
        try {
            return Configuration.ReporterConfiguration
                .fromEnv()
                .withLogSpans(true);
        } catch (RuntimeException e) {
            System.err.println("Error creating reporter configuation");
            e.printStackTrace();
            throw e;
        }
    }
}

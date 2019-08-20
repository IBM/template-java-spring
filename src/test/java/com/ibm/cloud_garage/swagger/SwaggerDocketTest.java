package com.ibm.cloud_garage.swagger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;

@DisplayName("SwaggerDocket")
public class SwaggerDocketTest {
    SwaggerConfig config;
    SwaggerDocket classUnderTest;
    SwaggerDocket classUnderTestSpy;

    @BeforeEach
    public void setup() {
        config = new SwaggerConfig();

        classUnderTest = new SwaggerDocket();
        ReflectionTestUtils.setField(classUnderTest, "config", config);

        classUnderTestSpy = spy(classUnderTest);
    }

    @Nested
    @DisplayName("Given api()")
    class GivenApi {
        @Nested
        @DisplayName("When called")
        class WhenCalled {
            @Test
            @DisplayName("Then build non-null Docket instance")
            void thenBuildNonNullDocketInstance() {
                assertNotNull(classUnderTest.api());
            }
        }
    }

    @Nested
    @DisplayName("Given buildApiRequestHandler()")
    public class GivenBuildApiRequestHandler {
        @Test
        public void empty_baseApiPackage_returns_any() {
            Predicate<RequestHandler> actual = classUnderTest.buildApiRequestHandler();

            assertEquals(RequestHandlerSelectors.any(), actual);
        }

        @Test
        public void nonEmpty_baseApiPackage_returns_basePackage() {
            final String baseApiPackage = "a.b.c";
            config.setBaseApiPackage(baseApiPackage);

            Predicate<RequestHandler> mock = mock(Predicate.class);

            doReturn(mock).when(classUnderTestSpy).buildBasePackageRequestHandler(anyString());

            Predicate<RequestHandler> actual = classUnderTestSpy.buildApiRequestHandler();

            assertEquals(mock, actual);
            verify(classUnderTestSpy).buildBasePackageRequestHandler(baseApiPackage);
        }
    }

    @Nested
    @DisplayName("Given guildBasePackageRequestHandler()")
    class GivenGuildBasePackageRequestHandler {
        @Nested
        @DisplayName("When called with a baseApiPackage")
        class WhenCalledWithABaseApiPackage {
            @Test
            @DisplayName("Then should return non-null result")
            void thenShouldReturnNonNullResult() {
                assertNotNull(classUnderTest.buildBasePackageRequestHandler("com.test"));
            }
        }
    }

    @Nested
    @DisplayName("Given buildApiInfo()")
    public class GivenBuildApiInfo {
        String title;
        String description;
        String version;
        String termsOfServiceUrl;
        String license;
        String licenseUrl;
        List<VendorExtension> vendorExtensions = new ArrayList<>();

        @BeforeEach
        public void setup() {
            config
                    .withTitle(title)
                    .withDescription(description)
                    .withVersion(version)
                    .withTermsOfServiceUrl(termsOfServiceUrl)
                    .withLicense(license)
                    .withLicenseUrl(licenseUrl)
                    .withVendorExtensions(vendorExtensions);
        }

        @Test
        @DisplayName("When config provided then it should return ApiInfo containing config values")
        public void return_value() {
            Contact contact = mock(Contact.class);
            doReturn(contact).when(classUnderTestSpy).buildContact();

            ApiInfo actual = classUnderTestSpy.buildApiInfo();

            assertEquals(title, actual.getTitle());
            assertEquals(description, actual.getDescription());
            assertEquals(version, actual.getVersion());
            assertEquals(termsOfServiceUrl, actual.getTermsOfServiceUrl());
            assertEquals(contact, actual.getContact());
            assertEquals(license, actual.getLicense());
            assertEquals(licenseUrl, actual.getLicenseUrl());
            assertEquals(vendorExtensions, actual.getVendorExtensions());
        }
    }

    @Nested
    @DisplayName("Given buildContact()")
    public class GivenBuildContact {
        @Test
        @DisplayName("When config.contact is null then it should return null")
        public void null_config_contact_should_return_null() {

            assertNull(classUnderTest.buildContact());
        }

        @Test
        @DisplayName("When config.contact is not null then it should return a Contact")
        public void nonNull_config_contact_should_return_contact() {
            final String url = "url";
            final String name = "name";
            final String email = "email";

            config.setContact(new SwaggerConfig.Contact()
                    .withUrl(url)
                    .withName(name)
                    .withEmail(email));

            Contact actual = classUnderTest.buildContact();

            assertEquals(url, actual.getUrl());
            assertEquals(name, actual.getName());
            assertEquals(email, actual.getEmail());
        }
    }
}

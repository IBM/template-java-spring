package com.ibm.cloud_native_toolkit.swagger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import springfox.documentation.service.VendorExtension;

@DisplayName("SwaggerConfig")
public class SwaggerConfigTest {
    @Test
    @DisplayName("Given getters When called Then they should return value")
    public void getters_setters() {
        List<String> baseApiPackages = List.of("basePackage");
        String title = "title";
        String description = "desc";
        String version = "0.1.0";
        String termsOfServiceUrl = "terms";
        SwaggerConfig.Contact contact = new SwaggerConfig.Contact();
        String license = "license";
        String licenseUrl = "licenseUrl";
        List<VendorExtension> vendorExtensions = new ArrayList<>();

        SwaggerConfig classUnderTest = new SwaggerConfig()
                .withBaseApiPackages(baseApiPackages)
                .withTitle(title)
                .withDescription(description)
                .withVersion(version)
                .withTermsOfServiceUrl(termsOfServiceUrl)
                .withContact(contact)
                .withLicense(license)
                .withLicenseUrl(licenseUrl)
                .withVendorExtensions(vendorExtensions);

        assertEquals(baseApiPackages, classUnderTest.getBaseApiPackages());
        assertEquals(title, classUnderTest.getTitle());
        assertEquals(description, classUnderTest.getDescription());
        assertEquals(version, classUnderTest.getVersion());
        assertEquals(termsOfServiceUrl, classUnderTest.getTermsOfServiceUrl());
        assertEquals(contact, classUnderTest.getContact());
        assertEquals(license, classUnderTest.getLicense());
        assertEquals(licenseUrl, classUnderTest.getLicenseUrl());
        assertEquals(vendorExtensions, classUnderTest.getVendorExtensions());
    }

    @Test
    public void vendor_extensions_handle_null() {

        SwaggerConfig classUnderTest = new SwaggerConfig();
        classUnderTest.setVendorExtensions(null);

        assertEquals(new ArrayList<>(), classUnderTest.getVendorExtensions());
    }

    @Nested
    @DisplayName("Given Contact")
    public class GivenContact {
        @Test
        @DisplayName("When getters called Then it should return values")
        public void getters_setters() {
            String email = "email";
            String name = "name";
            String url = "url";

            SwaggerConfig.Contact classUnderTest = new SwaggerConfig.Contact();
            classUnderTest.setEmail(email);
            classUnderTest.setName(name);
            classUnderTest.setUrl(url);

            assertEquals(email, classUnderTest.getEmail());
            assertEquals(name, classUnderTest.getName());
            assertEquals(url, classUnderTest.getUrl());
        }
    }
}

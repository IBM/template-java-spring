package com.ibm.cloud_native_toolkit.swagger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.VendorExtension;

@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfig {
    private Collection<String> baseApiPackages;
    private Collection<String> pathRegEx;
    private String title;
    private String description;
    private String version;
    private String termsOfServiceUrl;
    private Contact contact;
    private String license;
    private String licenseUrl;
    private final List<VendorExtension> vendorExtensions = new ArrayList<>();

    public SwaggerConfig() {
        super();
    }

    public Collection<String> getBaseApiPackages() {
        return baseApiPackages;
    }

    public void setBaseApiPackages(Collection<String> baseApiPackages) {
        this.baseApiPackages = baseApiPackages;
    }

    public SwaggerConfig withBaseApiPackages(Collection<String> baseApiPackages) {
        this.setBaseApiPackages(baseApiPackages);
        return this;
    }

    public Collection<String> getPathRegEx() {
        return pathRegEx;
    }

    public void setPathRegEx(Collection<String> pathRegEx) {
        this.pathRegEx = pathRegEx;
    }

    public SwaggerConfig withPathRegEx(Collection<String> pathRegEx) {
        this.setPathRegEx(pathRegEx);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SwaggerConfig withTitle(String title) {
        this.setTitle(title);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SwaggerConfig withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SwaggerConfig withVersion(String version) {
        this.setVersion(version);
        return this;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public SwaggerConfig withTermsOfServiceUrl(String termsOfServiceUrl) {
        this.setTermsOfServiceUrl(termsOfServiceUrl);
        return this;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public SwaggerConfig withContact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public SwaggerConfig withLicense(String license) {
        this.setLicense(license);
        return this;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public SwaggerConfig withLicenseUrl(String licenseUrl) {
        this.setLicenseUrl(licenseUrl);
        return this;
    }

    public List<VendorExtension> getVendorExtensions() {
        return Collections.unmodifiableList(vendorExtensions);
    }

    public void setVendorExtensions(List<VendorExtension> vendorExtensions) {
        this.vendorExtensions.clear();

        if (vendorExtensions != null) {
            this.vendorExtensions.addAll(vendorExtensions);
        }
    }

    public SwaggerConfig withVendorExtensions(List<VendorExtension> vendorExtensions) {
        this.setVendorExtensions(vendorExtensions);
        return this;
    }

    public static class Contact {
        private String name;
        private String url;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Contact withName(String name) {
            this.setName(name);
            return this;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Contact withUrl(String url) {
            this.setUrl(url);
            return this;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Contact withEmail(String email) {
            this.setEmail(email);
            return this;
        }
    }
}

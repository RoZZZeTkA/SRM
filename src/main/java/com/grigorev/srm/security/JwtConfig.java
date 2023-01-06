package com.grigorev.srm.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private Integer expirationAfterMinutes;
    private Integer expirationAfterDays;
    private Integer expirationAfterWeeks;
    private String prefix;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getExpirationAfterMinutes() {
        return expirationAfterMinutes;
    }

    public void setExpirationAfterMinutes(Integer expirationAfterMinutes) {
        this.expirationAfterMinutes = expirationAfterMinutes;
    }

    public Integer getExpirationAfterDays() {
        return expirationAfterDays;
    }

    public void setExpirationAfterDays(Integer expirationAfterDays) {
        this.expirationAfterDays = expirationAfterDays;
    }

    public Integer getExpirationAfterWeeks() {
        return expirationAfterWeeks;
    }

    public void setExpirationAfterWeeks(Integer expirationAfterWeeks) {
        this.expirationAfterWeeks = expirationAfterWeeks;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

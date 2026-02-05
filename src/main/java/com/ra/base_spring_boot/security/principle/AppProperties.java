package com.ra.base_spring_boot.security.principle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private Frontend frontend;
    private ResetPassword resetPassword;

    @Setter
    @Getter
    public static class Frontend {
        private String resetPasswordUrl;
    }

    @Getter
    @Setter
    public static class ResetPassword {
        private Integer expirationMinutes;
    }
}

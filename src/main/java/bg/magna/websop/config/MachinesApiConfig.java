package bg.magna.websop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "machines.api")
public class MachinesApiConfig {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public MachinesApiConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}

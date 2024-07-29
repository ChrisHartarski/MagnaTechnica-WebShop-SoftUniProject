package bg.magna.websop.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {
    @ConditionalOnProperty(value="rest.configuration", havingValue="prod")
    @Bean("machinesRestClient")
    public RestClient restClient(MachinesApiConfig machinesApiConfig) {
        return RestClient
                .builder()
                .baseUrl(machinesApiConfig.getBaseUrl())
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

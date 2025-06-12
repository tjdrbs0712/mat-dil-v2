package hello.matdil.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "portone")
@Getter
@Setter
public class PortOneProperties {
    private String apiKey;
    private String apiSecret;
}

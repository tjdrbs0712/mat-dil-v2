package hello.matdil.domain.payment.dto.portone;

public record PortoneTokenRequest(
        String imp_key,
        String imp_secret
) {}
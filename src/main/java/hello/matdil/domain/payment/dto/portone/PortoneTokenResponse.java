package hello.matdil.domain.payment.dto.portone;

public record PortoneTokenResponse(
        int code,
        String message,
        PortoneTokenData response
) {}

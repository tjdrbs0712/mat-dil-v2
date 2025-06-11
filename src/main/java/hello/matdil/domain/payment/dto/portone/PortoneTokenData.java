package hello.matdil.domain.payment.dto.portone;

public record PortoneTokenData(
        String access_token,
        long now,
        long expired_at
) {}

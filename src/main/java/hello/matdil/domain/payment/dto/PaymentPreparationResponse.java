//package hello.matdil.domain.payment.dto;
//
//import hello.matdil.config.PortOneProperties;
//import hello.matdil.domain.payment.entity.Payment;
//
//import java.math.BigDecimal;
//
//public record PaymentPreparationResponse(
//        Long paymentId,
//        BigDecimal amount,
//        String pgProvider,
//        String clientKey,
//        String storeId,
//        String channelKey
//) {
//    public static PaymentPreparationResponse from(
//            Payment payment,
//            String pgProvider,
//            PortOneProperties properties
//    ) {
//        return new PaymentPreparationResponse(
//                payment.getId(),
//                payment.getAmount(),
//                pgProvider,
//                properties.getClientKey(),
//                properties.getStoreId(),
//                properties.getChannelKey()
//        );
//    }
//}

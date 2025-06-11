package hello.matdil.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name("order-topic")
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name("notification-topic")
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentCompletedTopic() {
        return TopicBuilder.name("payment-completed-topic")
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderReadyForDispatch() {
        return TopicBuilder.name("order-ready-for-dispatch")
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic deliveryEventProducer() {
        return TopicBuilder.name("delivery-created-topic")
                .partitions(5)
                .replicas(1)
                .build();
    }
}
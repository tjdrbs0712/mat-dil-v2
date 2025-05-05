package hello.matdil.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringGenericEventPublisher implements GenericEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public <T> void publish(T event) {
        applicationEventPublisher.publishEvent(event);
    }
}
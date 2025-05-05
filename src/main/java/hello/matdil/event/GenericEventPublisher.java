package hello.matdil.event;

public interface GenericEventPublisher {
    <T> void publish(T event);
}
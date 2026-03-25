package com.banking.common.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class DomainEventPublisher {
    private final KafkaTemplate<String, BaseDomainEvent> kafkaTemplate;

    public DomainEventPublisher(KafkaTemplate<String, BaseDomainEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, BaseDomainEvent event) {
        kafkaTemplate.send(topic, event.getEventId(), event);
    }
}

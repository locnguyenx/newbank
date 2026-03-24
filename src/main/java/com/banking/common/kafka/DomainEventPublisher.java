package com.banking.common.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {
    private final KafkaTemplate<String, BaseDomainEvent> kafkaTemplate;

    public DomainEventPublisher(KafkaTemplate<String, BaseDomainEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, BaseDomainEvent event) {
        kafkaTemplate.send(topic, event.getEventId(), event);
    }
}

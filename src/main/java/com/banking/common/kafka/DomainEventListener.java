package com.banking.common.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class DomainEventListener {

    @KafkaListener(topics = "${spring.kafka.consumer.topics:domain-events}", groupId = "${spring.kafka.consumer.group-id:banking-consumer}")
    public void handleEvent(BaseDomainEvent event) {
    }
}

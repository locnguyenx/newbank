package com.banking.common.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DomainEventListener {

    @KafkaListener(topics = "${spring.kafka.consumer.topics:domain-events}", groupId = "${spring.kafka.consumer.group-id:banking-consumer}")
    public void handleEvent(BaseDomainEvent event) {
    }
}

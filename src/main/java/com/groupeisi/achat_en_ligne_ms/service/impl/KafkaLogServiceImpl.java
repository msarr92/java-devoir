package com.groupeisi.achat_en_ligne_ms.service.impl;

import com.groupeisi.achat_en_ligne_ms.service.KafkaLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaLogServiceImpl implements KafkaLogService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "achat-logs-topic";

    @Override
    public void sendLog(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
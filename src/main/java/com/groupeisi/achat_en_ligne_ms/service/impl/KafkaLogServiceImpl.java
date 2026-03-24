package com.groupeisi.achat_en_ligne_ms.service.impl;

import com.groupeisi.achat_en_ligne_ms.service.KafkaLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/*
 * Service pour envoyer des messages (logs) vers Kafka.
 * Kafka est utilisé ici pour tracer les actions (création, modification, suppression).
 */
@Service
@RequiredArgsConstructor
public class KafkaLogServiceImpl implements KafkaLogService {

    // KafkaTemplate permet d'envoyer des messages à Kafka
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Nom du topic Kafka où seront envoyés les logs
    private static final String TOPIC = "achat-logs-topic";

    /*
     * Méthode pour envoyer un message dans Kafka
     */
    @Override
    public void sendLog(String message) {

        // Envoie le message dans le topic "achat-logs-topic"
        kafkaTemplate.send(TOPIC, message);
    }
}
package com.ITECO.test_task_1.service.kafka;

import com.ITECO.test_task_1.common.dto.CommonKafkaRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Sender to Kafka for IDs batch.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IdKafkaSender {

    private final KafkaTemplate<String, CommonKafkaRecord> kafkaTemplate;

    @Value("${id-processor.kafka.topic-name}")
    private String topicName;

    /**
     * Sends accumulated IDs to Kafka and clears the collection.
     */
    public void sendIdsToKafka(List<String> idsToSend) {
        if (idsToSend.isEmpty()) {
            log.error("Can not send to Kafka: Empty ids list");
            return;
        }

        try {
            CommonKafkaRecord record = new CommonKafkaRecord(idsToSend);

            kafkaTemplate.send(topicName, record)
                    .whenComplete((_, ex) -> {
                        if (ex == null) {
                            log.info("Successfully sent {} IDs to Kafka topic {}", idsToSend.size(), topicName);
                        } else {
                            log.error("Failed to send IDs to Kafka", ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending IDs to Kafka", e);
        }
    }

}

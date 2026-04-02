package com.ITECO.test_task_1.service.kafka;

import com.ITECO.test_task_1.common.dto.CommonKafkaRecord;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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
public class IdKafkaSender {

    private final KafkaTemplate<String, CommonKafkaRecord> kafkaTemplate;
    //monitoring
    private final MeterRegistry registry;
    private final Counter batchCounter;
    private final Timer sendBatchTimer;

    @Value("${id-processor.kafka.topic-name}")
    private String topicName;


    public IdKafkaSender(KafkaTemplate<String, CommonKafkaRecord> kafkaTemplate, MeterRegistry registry) {
        this.kafkaTemplate = kafkaTemplate;
        this.registry = registry;

        this.batchCounter = registry.counter("id_service_batches_sent");
        this.sendBatchTimer = Timer.builder("id_service_kafka_sent_time")
                .publishPercentileHistogram()
                .register(registry);
    }

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

            Timer.Sample sample = Timer.start(registry);

            kafkaTemplate.send(topicName, record)
                    .whenComplete((_, ex) -> {
                        sample.stop(sendBatchTimer);

                        if (ex == null) {
                            batchCounter.increment();
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

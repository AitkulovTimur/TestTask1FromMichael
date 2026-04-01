package com.ITECO.test_task_1.service.kafka;


import com.ITECO.test_task_1.common.dto.CommonKafkaRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdKafkaReceiver {
    private final AtomicReference<List<String>> atomicCache = new AtomicReference<>(new ArrayList<>());

    @KafkaListener(
            topics = "${id-processor.kafka.topic-name}",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(CommonKafkaRecord record) {
        Collection<String> ids = record.ids();

        log.info("Received batch of ids: {}", ids);

        atomicCache.set(new ArrayList<>(ids));
    }

    public List<String> retrieveCachedIds() {
        return atomicCache.get();
    }
}

package com.ITECO.test_task_1.service;

import com.ITECO.test_task_1.common.VariablesHolder;
import com.ITECO.test_task_1.service.kafka.IdKafkaReceiver;
import com.ITECO.test_task_1.service.kafka.IdKafkaSender;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Service for processing IDs.
 */
@Slf4j
@Service
public class IdService {
    private final LinkedBlockingQueue<String> ids = new LinkedBlockingQueue<>();

    private final IdKafkaSender idKafkaSender;
    private final VariablesHolder variablesHolder;
    private final IdKafkaReceiver idKafkaReceiver;

    public IdService(MeterRegistry registry, IdKafkaSender idKafkaSender, VariablesHolder variablesHolder, IdKafkaReceiver idKafkaReceiver) {
        this.idKafkaSender = idKafkaSender;
        this.variablesHolder = variablesHolder;
        this.idKafkaReceiver = idKafkaReceiver;

        Gauge.builder("id_service_queue_size", ids, LinkedBlockingQueue::size).register(registry);
    }

    /**
     * Adds an ID to the collection.
     *
     * @param id the ID to add
     */
    public void addId(String id) {
        log.info("Adding id to general collection: {}", id);

        ids.add(id);

        int batchSize = variablesHolder.getBatchSize();
        if (ids.size() >= batchSize) {
            List<String> batch = new ArrayList<>();
            // Только 1 поток реально заберет элементы, остальные получат пустоту
            //1 захватил lock -> другие просто ждут освобождения и потом просто получают 0 элементов
            ids.drainTo(batch, batchSize);

            if (!batch.isEmpty()) { // страховка
                idKafkaSender.sendIdsToKafka(batch);
            }
        }
    }

    /**
     * Retrieves all IDs from the collection.
     *
     * @return a collection of all stored IDs
     */
    public Collection<String> getAllIds() {
        log.info("Returning all ids from the cached collection");

        return idKafkaReceiver.retrieveCachedIds();
    }

}

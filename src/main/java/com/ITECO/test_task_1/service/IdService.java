package com.ITECO.test_task_1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Service for processing IDs.
 */
@Slf4j
@Service
public class IdService {
    private final Queue<String> ids = new ConcurrentLinkedQueue<>();

    /**
     * Adds an ID to the collection.
     * @param id the ID to add
     */
    public void addId(String id) {
        log.info("Adding id to general collection: {}", id);

        ids.add(id);
    }

    /**
     * Retrieves all IDs from the collection.
     * @return a collection of all stored IDs
     */
    public Collection<String> getAllIds() {
        log.info("Returning all ids from general collection");

        return ids;
    }


}

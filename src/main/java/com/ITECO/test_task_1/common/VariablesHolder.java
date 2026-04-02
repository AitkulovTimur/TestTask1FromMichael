package com.ITECO.test_task_1.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class VariablesHolder {
    @Value("${id-processor.batch-size}")
    private int batchSize;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.application.name}")
    private String appName;
}

package com.ITECO.test_task_1.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request object for adding an ID.
 */
public record IdAddRequest(
        @NotBlank(message = "Id must not be null")
        String id) {
}

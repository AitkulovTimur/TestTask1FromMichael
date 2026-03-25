package com.ITECO.test_task_1.dto;

/**
 * General class for responses.
 *
 * @param response
 * @param <T>
 */
public record GeneralResponse<T>(T response) {
}

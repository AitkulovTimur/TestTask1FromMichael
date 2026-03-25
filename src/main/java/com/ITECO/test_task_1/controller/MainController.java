package com.ITECO.test_task_1.controller;

import com.ITECO.test_task_1.dto.GeneralResponse;
import com.ITECO.test_task_1.dto.IdAddRequest;
import com.ITECO.test_task_1.service.IdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Controller for main test operations.
 */
@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class MainController {
    private final IdService idService;

    /**
     * Adds a new ID to the collection.
     *
     * @param idAddRequest the request containing the ID to add
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse<String>> addId(@Valid @RequestBody IdAddRequest idAddRequest) {
        String idToAdd = idAddRequest.id();
        idService.addId(idToAdd);
        String response = String.format("Entity with id %s added successfully", idToAdd);

        return new ResponseEntity<>(new GeneralResponse<>(response), HttpStatus.OK);
    }

    /**
     * Retrieves all stored IDs.
     *
     * @return a collection of all IDs
     */
    @GetMapping
    public ResponseEntity<GeneralResponse<Collection<String>>> getAllIds() {
        return new ResponseEntity<>(new GeneralResponse<>(idService.getAllIds()), HttpStatus.OK);
    }

}

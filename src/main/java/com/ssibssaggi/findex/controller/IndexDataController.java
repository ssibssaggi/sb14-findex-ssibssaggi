package com.ssibssaggi.findex.controller;

import com.ssibssaggi.findex.application.index.IndexDataApplication;
import com.ssibssaggi.findex.controller.dto.IndexDataCreateRequest;
import com.ssibssaggi.findex.controller.dto.IndexDataResponse;
import com.ssibssaggi.findex.controller.dto.IndexDataUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
public class IndexDataController {

    private final IndexDataApplication indexDataApplication;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public IndexDataResponse createIndexData(
        @RequestBody IndexDataCreateRequest createRequest) {
        return indexDataApplication.saveData(createRequest.toCommand());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id}")
    public IndexDataResponse updateIndexData(
        @PathVariable Long id, @RequestBody IndexDataUpdateRequest request) {
        return indexDataApplication.update(id, request.toCommand());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteIndexData(@PathVariable Long id) {
        indexDataApplication.delete(id);
    }
}
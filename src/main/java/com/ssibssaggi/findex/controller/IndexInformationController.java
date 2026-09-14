package com.ssibssaggi.findex.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssibssaggi.findex.application.index.IndexInformationApplication;
import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.CursorPaginationCondition;
import com.ssibssaggi.findex.controller.dto.IndexInfoCreateRequest;
import com.ssibssaggi.findex.controller.dto.IndexInfoFilterCondition;
import com.ssibssaggi.findex.controller.dto.IndexInfoUpdateRequest;
import com.ssibssaggi.findex.controller.dto.IndexInformationResponse;
import com.ssibssaggi.findex.controller.dto.IndexInformationSummaryResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndexInformationController {
    private final IndexInformationApplication indexInformationApplication;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/index-infos")
    public CursorPageResult<IndexInformationResponse> getInfos(
            @RequestParam(required = false) String indexClassification,
            @RequestParam(required = false) String indexName,
            @RequestParam(required = false) Boolean favorite,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "indexClassification") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        IndexInfoFilterCondition indexInfoFilterCondition = new IndexInfoFilterCondition(
                indexClassification,
                indexName,
                favorite
        );
        CursorPaginationCondition cursorPaginationCondition = new CursorPaginationCondition(
                idAfter,
                cursor,
                sortField,
                sortDirection,
                size
        );
        return indexInformationApplication.searchIndexInfos(indexInfoFilterCondition, cursorPaginationCondition);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/api/index-infos")
    public IndexInformationResponse createIndexInfo(
            @RequestBody IndexInfoCreateRequest indexInfoCreateRequest
    ) {
        return indexInformationApplication.saveInformation(indexInfoCreateRequest.toCommand());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/index-infos/{id}")
    public IndexInformationResponse getIndexInfoById(
            @PathVariable Long id
    ) {
        return indexInformationApplication.findById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/api/index-infos/{id}")
    public void deleteIndexInfoById(
            @PathVariable Long id
    ) {
        indexInformationApplication.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/api/index-infos/{id}")
    public IndexInformationResponse updateIndexInfoById(
            @PathVariable Long id,
            @RequestBody IndexInfoUpdateRequest indexInfoUpdateRequest
    ) {
        return indexInformationApplication.update(id, indexInfoUpdateRequest.toCommand());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/index-infos/summaries")
    public List<IndexInformationSummaryResponse> getIndexInfoSummaries() {
        return indexInformationApplication.findSummary();
    }
}

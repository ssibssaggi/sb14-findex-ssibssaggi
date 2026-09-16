package com.ssibssaggi.findex.controller.swagger;

import java.util.List;

import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.IndexInfoCreateRequest;
import com.ssibssaggi.findex.controller.dto.IndexInfoUpdateRequest;
import com.ssibssaggi.findex.controller.dto.IndexInformationResponse;
import com.ssibssaggi.findex.controller.dto.IndexInformationSummaryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지수 정보 관리", description = "지수 정보 관리 API")
public interface IndexInformationControllerSwagger {

    @Operation(summary = "지수 정보 목록 조회", description = "지수 정보 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiInternalServerErrorResponse
    CursorPageResult<IndexInformationResponse> getInfos(
            @Parameter(description = "지수 분류명") String indexClassification,
            @Parameter(description = "지수명") String indexName,
            @Parameter(description = "관심 지수 여부") Boolean favorite,
            @Parameter(description = "이전 페이지 마지막 요소의 ID") Long idAfter,
            @Parameter(description = "커서(다음 페이지 조회용)") String cursor,
            @Parameter(description = "정렬 필드") String sortField,
            @Parameter(description = "정렬 방향(asc/desc)") String sortDirection,
            @Parameter(description = "페이지 크기") Integer size
    );

    @Operation(summary = "지수 정보 등록", description = "새로운 지수 정보를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공")
    })
    @ApiBadRequestResponse
    @ApiInternalServerErrorResponse
    IndexInformationResponse createIndexInfo(
            IndexInfoCreateRequest indexInfoCreateRequest
    );

    @Operation(summary = "지수 정보 단건 조회", description = "ID로 지수 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiNotFoundResponse(description = "지수 정보를 찾을 수 없음")
    @ApiInternalServerErrorResponse
    IndexInformationResponse getIndexInfoById(
            @Parameter(description = "지수 정보 ID") Long id
    );

    @Operation(summary = "지수 정보 삭제", description = "지수 정보를 삭제합니다. 관련된 지수 데이터도 함께 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    @ApiNotFoundResponse(description = "지수 정보를 찾을 수 없음")
    @ApiInternalServerErrorResponse
    void deleteIndexInfoById(
            @Parameter(description = "지수 정보 ID") Long id
    );

    @Operation(summary = "지수 정보 수정", description = "기존 지수 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @ApiNotFoundResponse(description = "지수 정보를 찾을 수 없음")
    @ApiInternalServerErrorResponse
    IndexInformationResponse updateIndexInfoById(
            @Parameter(description = "지수 정보 ID") Long id,
            IndexInfoUpdateRequest indexInfoUpdateRequest
    );

    @Operation(summary = "지수 정보 요약 목록 조회", description = "지수 ID, 분류, 이름만 포함한 전체 지수 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiInternalServerErrorResponse
    List<IndexInformationSummaryResponse> getIndexInfoSummaries();
}

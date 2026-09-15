package com.ssibssaggi.findex.controller.swagger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.IndexDataSyncRequest;
import com.ssibssaggi.findex.controller.dto.SyncJobDto;
import com.ssibssaggi.findex.domain.entity.integrationhistory.IntegrationResult;
import com.ssibssaggi.findex.domain.entity.integrationhistory.JobType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지수 연동 관리", description = "연동 작업 관리 API")
public interface IndexIntegrationControllerSwagger {

    @Operation(summary = "지수 정보 연동", description = "Open API를 통해 지수 정보를 연동합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "연동 작업 접수 성공")
    })
    @ApiInternalServerErrorResponse
    List<SyncJobDto> syncIndexInfo(HttpServletRequest request);

    @Operation(summary = "지수 데이터 연동", description = "Open API를 통해 지수 데이터를 연동합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "연동 작업 접수 성공")
    })
    @ApiInternalServerErrorResponse
    List<SyncJobDto> syncIndexData(
            HttpServletRequest request,
            IndexDataSyncRequest indexDataSyncRequest
    );

    @Operation(summary = "연동 작업 목록 조회", description = "연동 작업 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiInternalServerErrorResponse
    CursorPageResult<SyncJobDto> searchSyncJobs(
            @Parameter(description = "작업 유형") JobType jobType,
            @Parameter(description = "지수 정보 ID") Long indexInfoId,
            @Parameter(description = "대상 일자 시작") LocalDate baseDateFrom,
            @Parameter(description = "대상 일자 종료") LocalDate baseDateTo,
            @Parameter(description = "작업자") String worker,
            @Parameter(description = "작업 시각 시작") LocalDateTime jobTimeFrom,
            @Parameter(description = "작업 시각 종료") LocalDateTime jobTimeTo,
            @Parameter(description = "작업 결과 상태") IntegrationResult status,
            @Parameter(description = "이전 페이지 마지막 요소의 ID") Long idAfter,
            @Parameter(description = "커서(다음 페이지 조회용)") String cursor,
            @Parameter(description = "정렬 필드") String sortField,
            @Parameter(description = "정렬 방향(asc/desc)") String sortDirection,
            @Parameter(description = "페이지 크기") Integer size
    );
}

package com.ssibssaggi.findex.controller.swagger;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.IndexChartResponse;
import com.ssibssaggi.findex.controller.dto.IndexDataCreateRequest;
import com.ssibssaggi.findex.controller.dto.IndexDataResponse;
import com.ssibssaggi.findex.controller.dto.IndexDataUpdateRequest;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceFavoriteResponse;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceRankResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지수 데이터 관리", description = "지수 데이터(IndexData) 등록/조회/수정/삭제 및 통계 API")
public interface IndexDataControllerSwagger {

    @Operation(summary = "지수 데이터 CSV 내보내기", description = "조건에 맞는 지수 데이터를 CSV 파일로 내보냅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내보내기 성공")
    })
    @ApiInternalServerErrorResponse
    void export(
            @Parameter(description = "지수 정보 ID") Long indexInformationId,
            @Parameter(description = "조회 시작일") LocalDate startDate,
            @Parameter(description = "조회 종료일") LocalDate endDate,
            @Parameter(description = "정렬 필드") String sortField,
            @Parameter(description = "정렬 방향(asc/desc)") String sortDirection,
            HttpServletResponse response
    ) throws Exception;

    @Operation(summary = "지수 데이터 목록 조회", description = "조건에 맞는 지수 데이터를 커서 기반 페이지네이션으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiInternalServerErrorResponse
    CursorPageResult<IndexDataResponse> getInfos(
            @Parameter(description = "지수 정보 ID") Long indexInfoId,
            @Parameter(description = "조회 시작일") LocalDate startDate,
            @Parameter(description = "조회 종료일") LocalDate endDate,
            @Parameter(description = "이전 페이지 마지막 요소의 ID") Long idAfter,
            @Parameter(description = "커서(다음 페이지 조회용)") String cursor,
            @Parameter(description = "정렬 필드") String sortField,
            @Parameter(description = "정렬 방향(asc/desc)") String sortDirection,
            @Parameter(description = "페이지 크기") Integer size
    );

    @Operation(summary = "지수 데이터 등록", description = "새로운 지수 데이터를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공")
    })
    @ApiBadRequestResponse
    @ApiNotFoundResponse(description = "지수 정보를 찾을 수 없음")
    @ApiInternalServerErrorResponse
    IndexDataResponse createIndexData(
            IndexDataCreateRequest createRequest
    );

    @Operation(summary = "지수 데이터 수정", description = "ID로 지수 데이터를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @ApiNotFoundResponse(description = "지수 데이터를 찾을 수 없음")
    @ApiInternalServerErrorResponse
    IndexDataResponse updateIndexData(
            @Parameter(description = "지수 데이터 ID") Long id,
            IndexDataUpdateRequest request
    );

    @Operation(summary = "지수 데이터 삭제", description = "ID로 지수 데이터를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    @ApiNotFoundResponse(description = "지수 데이터를 찾을 수 없음")
    @ApiInternalServerErrorResponse
    void deleteIndexData(
            @Parameter(description = "지수 데이터 ID") Long id
    );

    @Operation(summary = "지수 성과 랭킹 조회", description = "기간 유형별 지수 등락 성과 랭킹을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiBadRequestResponse
    @ApiInternalServerErrorResponse
    List<IndexPerformanceRankResponse> getPerformanceRanking(
            @Parameter(description = "지수 정보 ID") Long indexInfoId,
            @Parameter(description = "기간 유형(DAILY, WEEKLY, MONTHLY 등)") String periodType,
            @Parameter(description = "조회 개수") Integer limit
    );

    @Operation(summary = "지수 차트 데이터 조회", description = "ID와 기간 유형에 따른 지수 차트 데이터(이동 평균선 포함)를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiBadRequestResponse
    @ApiNotFoundResponse(description = "지수 정보를 찾을 수 없음")
    @ApiInternalServerErrorResponse
    IndexChartResponse getIndexChartData(
            @Parameter(description = "지수 정보 ID") Long id,
            @Parameter(description = "기간 유형(DAILY, WEEKLY, MONTHLY 등)") String periodType
    );

    @Operation(summary = "관심 지수 성과 조회", description = "즐겨찾기(관심) 지수의 등락 성과를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiBadRequestResponse
    @ApiInternalServerErrorResponse
    List<IndexPerformanceFavoriteResponse> getFavoritePerformance(
            @Parameter(description = "기간 유형(DAILY, WEEKLY, MONTHLY 등)") String periodType
    );
}

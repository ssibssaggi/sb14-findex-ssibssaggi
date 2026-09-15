package com.ssibssaggi.findex.controller.swagger;

import com.ssibssaggi.findex.common.dto.CursorPageResult;
import com.ssibssaggi.findex.controller.dto.AutoSyncConfigDto;
import com.ssibssaggi.findex.controller.dto.AutoSyncConfigUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "자동 연동 설정 관리", description = "자동 연동 설정 관리 API")
public interface AutoSyncControllerSwagger {

    @Operation(summary = "자동 연동 설정 수정", description = "기존 자동 연동 설정을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @ApiNotFoundResponse(description = "자동 연동 설정을 찾을 수 없음")
    @ApiInternalServerErrorResponse
    AutoSyncConfigDto updateAutoSyncConfig(
            @Parameter(description = "자동 연동 설정 ID") Long id,
            AutoSyncConfigUpdateRequest request
    );

    @Operation(summary = "자동 연동 설정 목록 조회", description = "자동 연동 설정 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @ApiInternalServerErrorResponse
    CursorPageResult<AutoSyncConfigDto> searchAutoSyncConfigs(
            @Parameter(description = "지수 정보 ID") Long indexInfoId,
            @Parameter(description = "자동 연동 활성화 여부") Boolean enabled,
            @Parameter(description = "이전 페이지 마지막 요소의 ID") Long idAfter,
            @Parameter(description = "커서(다음 페이지 조회용)") String cursor,
            @Parameter(description = "정렬 필드") String sortField,
            @Parameter(description = "정렬 방향(asc/desc)") String sortDirection,
            @Parameter(description = "페이지 크기") Integer size
    );
}

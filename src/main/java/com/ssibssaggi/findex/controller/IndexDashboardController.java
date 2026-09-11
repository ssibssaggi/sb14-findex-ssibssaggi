package com.ssibssaggi.findex.controller;

import com.ssibssaggi.findex.application.IndexDashboardApplication;
import com.ssibssaggi.findex.application.indexintegration.IndexIntegrationApplication;
import com.ssibssaggi.findex.controller.dto.IndexPerformanceFavoriteResponse;
import com.ssibssaggi.findex.controller.dto.SyncJobDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IndexDashboardController {

    private final IndexDashboardApplication indexDashboardApplication;

    @ResponseStatus(HttpStatus.OK) // 스웨거를 보면 상태코드가 200이므로 HttpStatus.OK
    @GetMapping("/api/index-data/performance/favorite") //스웨거를 보면 Get으로 받아온다
    public List<IndexPerformanceFavoriteResponse> getFavoritePerformance() {
        return indexDashboardApplication.getFavoritePerformance();
    }
}

//컨트롤러 다 하면 커밋 넘어가고 안돼는 다시하기
//대시보드의 어플리케이션의 쓸 수 있는 코드를 파악 어플리케이션, 즐겨찾기 리스폰, 어플리케이션의 즐겨찾기 가져오기
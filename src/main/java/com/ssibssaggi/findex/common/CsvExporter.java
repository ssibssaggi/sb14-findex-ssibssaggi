package com.ssibssaggi.findex.common;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.ssibssaggi.findex.common.exception.CustomException;
import com.ssibssaggi.findex.controller.dto.IndexDataExportDto;

@Component
public class CsvExporter {
    public void writeCsv(OutputStream out, List<IndexDataExportDto> exportData) {
        try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            HeaderColumnNameMappingStrategy<IndexDataExportDto> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(IndexDataExportDto.class);
            List<String> headerOrder = List.of(
                    "기준일자", "시가", "종가", "고가", "저가", "전일대비등락", "등락률", "거래량", "거래대금", "시가총액"
            );
            strategy.setColumnOrderOnWrite(Comparator.comparing(headerOrder::indexOf));

            StatefulBeanToCsv<IndexDataExportDto> beanToCsv =
                    new StatefulBeanToCsvBuilder<IndexDataExportDto>(writer)
                            .withApplyQuotesToAll(false)
                            .withMappingStrategy(strategy)
                            .build();
            beanToCsv.write(exportData);
            writer.flush();
        } catch (Exception e) {
            throw new CustomException("잘못된 요청입니다.", HttpStatus.INTERNAL_SERVER_ERROR, "CSV 파일 내보내기 중 오류가 발생했습니다.");
        }
    }
}

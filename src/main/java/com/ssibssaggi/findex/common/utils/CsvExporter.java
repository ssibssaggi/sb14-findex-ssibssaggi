package com.ssibssaggi.findex.common.utils;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.ssibssaggi.findex.common.exception.CustomException;
import com.ssibssaggi.findex.common.exception.ErrorStatus;

public class CsvExporter {
    public static <T> void writeCsv(
            OutputStream out,
            List<T> exportData,
            List<String> headerOrder,
            Class<T> type

    ) {
        try (Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(type);
            strategy.setColumnOrderOnWrite(Comparator.comparing(headerOrder::indexOf));

            StatefulBeanToCsv<T> beanToCsv =
                    new StatefulBeanToCsvBuilder<T>(writer)
                            .withApplyQuotesToAll(false)
                            .withMappingStrategy(strategy)
                            .build();

            beanToCsv.write(exportData);
            writer.flush();
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.CSV_EXPORT_FAILED, "CSV 파일 내보내기 중 오류가 발생했습니다.");
        }
    }
}

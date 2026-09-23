package com.tsm.report_generator_api.exporter.csv;

import com.tsm.report_generator_api.dto.ReportDataDto;
import com.tsm.report_generator_api.exception.GenerationErrorException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ApacheCommonsCsvExporter {
    public byte[] export(String reportName, ReportDataDto data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setDelimiter(';').get())) {

            List<Map<String, Object>> rows = data.getRows() != null
                    ? data.getRows()
                    : List.of();

            if (rows.isEmpty()) {
                printer.flush();
                writer.flush();
                return baos.toByteArray();
            }

            // CSV Header
            List<String> columns = new ArrayList<>(rows.get(0).keySet());
            printer.printRecord(columns);

            // CSV Data
            for (Map<String, Object> row : rows) {
                List<Object> values = new ArrayList<>(columns.size());
                for (String column : columns) {
                    values.add(row.get(column));
                }
                printer.printRecord(values);
            }

            printer.flush();
            writer.flush();
            return baos.toByteArray();

        } catch (IOException e) {
            throw new GenerationErrorException("CSV Generating error");
        }
    }
}
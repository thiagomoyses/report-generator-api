package com.tsm.report_generator_api.exporter.excel;

import com.tsm.report_generator_api.dto.ReportDataDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ApachePoiExporter {
    public byte[] export(String reportName, ReportDataDto data) {

        String sheetName = reportName != null
                ? reportName
                : "reportData";

        try(Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            List<Map<String, Object>> rows = data.getRows() != null
                    ? data.getRows()
                    : List.of();

            if (rows.isEmpty()) {
                writeHeaderRow(sheet, List.of());
                workbook.write(baos);
                return baos.toByteArray();
            }

            // Extract column name from data
            List<String> columns = new ArrayList<>(rows.getFirst().keySet());
            writeHeaderRow(sheet, columns);

            // writing data
            int indexRow = 1;
            for (Map<String, Object> row : rows) {
                Row sheetRow = sheet.createRow(indexRow++);
                for (int indexColumn = 0; indexColumn < columns.size(); indexColumn++){
                    Object value = row.get(columns.get(indexColumn));
                    Cell cell = sheetRow.createCell(indexColumn);
                    setValueToCell(cell, value);
                }
            }

            // Columns Auto-size
            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error generating XLSX: ", e);
        }
    }

    private void setValueToCell(Cell cell, Object value) {
        switch (value) {
            case null -> {
                cell.setBlank();
            }
            case Number n -> cell.setCellValue(n.doubleValue());
            case Boolean b -> cell.setCellValue(b);
            default -> cell.setCellValue(value.toString());
        }
    }

    private void writeHeaderRow(Sheet sheet, List<String> columns) {
        Row header = sheet.createRow(0);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font bold = sheet.getWorkbook().createFont();

        bold.setBold(true);
        headerStyle.setFont(bold);


        for (int i = 0; i < columns.size(); i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerStyle);
        }
    }
}

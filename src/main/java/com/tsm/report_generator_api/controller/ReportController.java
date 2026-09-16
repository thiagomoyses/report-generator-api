package com.tsm.report_generator_api.controller;

import com.tsm.report_generator_api.facade.ReportFacade;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportFacade facade;

    public ReportController(ReportFacade facade){
        this.facade = facade;
    }

    @GetMapping("/{reportName}/{format}")
    public ResponseEntity<byte[]> generateReport(
            @PathVariable String reportName,
            @PathVariable String format,
            @RequestParam Map<String, String> params)
    {
        params.remove("format");
        byte[] reportBytes = facade.generateReport(reportName, params, format);

        String contentType = switch (format.toLowerCase()) {
            case "pdf" -> "application/pdf";
            case "csv" -> "text/csv";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default -> "application/octet-stream";
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + reportName + "." + format)
                .body(reportBytes);
    }
}

package com.tsm.report_generator_api.controller;

import com.tsm.report_generator_api.facade.ReportFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Generate a new report",
            description = "Generates a report in the requested format."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Report generated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid report parameters"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Report not found"
            )
    })
    @GetMapping("/{reportName}/{format}")
    public ResponseEntity<byte[]> generateReport(
            @PathVariable String reportName,
            @PathVariable String format,
            @RequestParam(required = false) Map<String, String> params)
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

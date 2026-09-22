package com.tsm.report_generator_api.service.student;

import com.tsm.report_generator_api.dto.ReportDataDto;
import com.tsm.report_generator_api.exporter.pdf.JasperExporter;
import com.tsm.report_generator_api.repository.student.FindAllStudentsRepository;
import com.tsm.report_generator_api.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class FindAllStudentsService implements ReportService {
    private final FindAllStudentsRepository findAllStudentsRepository;
    private final JasperExporter exporterPdf;

    public FindAllStudentsService(
            FindAllStudentsRepository findAllStudentsRepository,
            JasperExporter exporterPdf
    ) {
        this.findAllStudentsRepository = findAllStudentsRepository;
        this.exporterPdf = exporterPdf;
    }

    @Override
    public byte[] generateReport(Map<String, String> reportParams, String format) {

        // Get rows
        List<Map<String, Object>> rows = findAllStudentsRepository.find();

        // Get Exec time
        String execTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        ReportDataDto data = new ReportDataDto();
        data.setHeader(Map.of(
                "reportName", "findAllStudents",
                "execTime", execTime,
                "title", "Students List"
        ));

        data.setRows(rows);

        return exporterPdf.export("findallstudents", data, format);
    }
}

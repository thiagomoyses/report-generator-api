package com.tsm.report_generator_api.facade;

import com.tsm.report_generator_api.service.ReportService;
import com.tsm.report_generator_api.service.student.FindAllStudentsService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReportFacadeImpl implements ReportFacade{
    private final Map<String, ReportService> serviceMap = new ConcurrentHashMap<>();

    public ReportFacadeImpl(
            FindAllStudentsService findAllStudentsService
    ) {
        serviceMap.put("findallstudents", findAllStudentsService);
    }

    @Override
    public byte[] generateReport(String reportName, Map<String, String> reportParams, String format){
        ReportService service = serviceMap.get(reportName);
        if (service == null) {
            throw new IllegalArgumentException("Report not found: " + reportName);
        }
        return service.generateReport(reportParams, format);
    }
}

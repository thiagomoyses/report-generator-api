package com.tsm.report_generator_api.facade;

import com.tsm.report_generator_api.service.ReportService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReportFacadeImpl implements ReportFacade{
    private final Map<String, ReportService> serviceMap = new ConcurrentHashMap<>();

    public ReportFacadeImpl(

    ) {}

    @Override
    public byte[] generateReport(String reportName, Map<String, String> reportParams, String format){
        ReportService service = serviceMap.get(reportName);
        if (service == null) {
            throw new IllegalArgumentException("Report not found: " + reportName);
        }
        return service.generateReport(reportParams, format);
    }
}

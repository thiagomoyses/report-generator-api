package com.tsm.report_generator_api.facade;

import java.util.Map;

public interface ReportFacade {
    byte[] generateReport(String reportName, Map<String, String> params, String format);
}

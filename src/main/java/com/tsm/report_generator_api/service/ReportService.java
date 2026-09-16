package com.tsm.report_generator_api.service;

import java.util.Map;

public interface ReportService {
    byte[] generateReport(Map<String, String> reportParams, String format);
}
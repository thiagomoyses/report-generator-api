package com.tsm.report_generator_api.dto;

import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.util.List;
import java.util.Map;

@Data
public class ReportDataDto {
    private Map<String, Object> header;
    private List<Map<String, Object>> rows;
    private Map<String, Object> footer;
}

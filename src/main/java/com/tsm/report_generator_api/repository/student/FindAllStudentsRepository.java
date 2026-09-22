package com.tsm.report_generator_api.repository.student;

import com.tsm.report_generator_api.repository.ReportRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FindAllStudentsRepository {
    private final ReportRepository reportRepository;

    public FindAllStudentsRepository(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Map<String, Object>> find() {
        StringBuilder query = new StringBuilder("""
                SELECT
                    ID,
                    NAME,
                    EMAIL,
                    BIRTH_DATE,
                    CREATED_AT
                FROM
                    students;
                """);

        return reportRepository.executeQuery(query.toString());
    }
}

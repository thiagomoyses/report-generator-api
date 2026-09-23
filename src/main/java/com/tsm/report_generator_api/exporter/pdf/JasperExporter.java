package com.tsm.report_generator_api.exporter.pdf;

import com.tsm.report_generator_api.dto.ReportDataDto;
import com.tsm.report_generator_api.exception.GenerationErrorException;
import com.tsm.report_generator_api.exception.NotFoundException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class JasperExporter {
    private static final String TEMPLATE_PATH = "reports/";

    public byte[] export(String reportName, ReportDataDto data) {
        try {
            // Load .jasper compilated from classpath
            InputStream templateStream = getClass().getClassLoader()
                    .getResourceAsStream(TEMPLATE_PATH + reportName + ".jasper");

            if (templateStream == null) {
                throw new NotFoundException("Report template not found");
            }
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateStream);

            // Fill report with data (Using JRBeanCollectionDataSource)
            JRDataSource dataSource = buildDatasource(data);

            Map<String, Object> jasperParams = new HashMap<>();

            if (data.getHeader() != null) {
                jasperParams.put("header", data.getHeader());
            }

            if (data.getFooter() != null) {
                jasperParams.put("footer", data.getFooter());
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParams, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new GenerationErrorException("Error generation report");
        }
    }

    @SuppressWarnings("unchecked")
    private JRDataSource buildDatasource(ReportDataDto data) {
        Collection<Map<String, ?>> rows = (Collection<Map<String, ?>>) (Collection<?>) data.getRows();

        return new JRMapCollectionDataSource(rows);
    }

    private byte[] exportToCsv(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(baos));
        exporter.exportReport();
        return baos.toByteArray();
    }
}

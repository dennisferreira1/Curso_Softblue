package br.com.sw2you.realmeet.model;

import br.com.sw2you.realmeet.report.model.GeneratedReport;
import br.com.sw2you.realmeet.service.ReportDispatcherService;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class TestReportDispatcherService extends ReportDispatcherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestReportDispatcherService.class);
    private static final String TEM_DIR = System.getProperty("java.io.tmpdir");

    public TestReportDispatcherService() {
        super(null, null);
    }

    @Override
    public void dispatch(GeneratedReport generatedReport) {
        var outputFile = new File(TEM_DIR, generatedReport.getFileName());
        try {
            FileCopyUtils.copy(generatedReport.getBytes(), outputFile);
            LOGGER.info("Report saved to {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Error saving report to: " + outputFile.getAbsolutePath(), e);
        }
    }
}

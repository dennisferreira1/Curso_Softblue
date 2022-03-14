package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.report.enumeration.ReportFormat;
import br.com.sw2you.realmeet.report.enumeration.ReportHandlerType;
import br.com.sw2you.realmeet.report.model.AbstractReportData;
import br.com.sw2you.realmeet.report.model.AllocationReportData;
import br.com.sw2you.realmeet.report.model.GeneratedReport;
import br.com.sw2you.realmeet.report.resolver.ReportHandlerResolver;
import br.com.sw2you.realmeet.util.Constants;
import java.time.LocalDate;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportCreationService {
    private final ReportHandlerResolver reportHandlerResolver;
    private final ReportDispatcherService reportDispatcherService;

    public ReportCreationService(
        ReportHandlerResolver reportHandlerResolver,
        ReportDispatcherService reportDispatcherService
    ) {
        this.reportHandlerResolver = reportHandlerResolver;
        this.reportDispatcherService = reportDispatcherService;
    }

    @Transactional(readOnly = true)
    public void createAllocationReport(LocalDate dateFrom, LocalDate dateTo, String email, String reportFormatStr) {
        var allocationReportData = AllocationReportData
            .newAllocationDataReportBuilder()
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .email(email)
            .build();

        createReport(email, reportFormatStr, allocationReportData, ReportHandlerType.ALLOCATION);
    }

    private void createReport(
        String email,
        String reportFormatStr,
        AbstractReportData reportData,
        ReportHandlerType reportHandlerType
    ) {
        var reportFormat = ReportFormat.fromString(reportFormatStr);
        var reportHandler = reportHandlerResolver.resolveReportHandler(reportHandlerType);
        reportHandler.getReportValidator().validate(reportData);
        var bytes = reportHandler.createReportBytes(reportData, reportFormat);

        reportDispatcherService.dispatch(
            GeneratedReport
                .newGeneratedReportBuilder()
                .emailTo(email)
                .reportFormat(reportFormat)
                .templateType(reportHandler.getTemplateType())
                .bytes(bytes)
                .fileName(buildFileName(reportHandlerType, reportFormat))
                .build()
        );
    }

    private String buildFileName(ReportHandlerType reportHandlerType, ReportFormat reportFormat) {
        return (Constants.REPORT + reportHandlerType.name().toLowerCase(Locale.ROOT) + reportFormat.getExtension());
    }
}

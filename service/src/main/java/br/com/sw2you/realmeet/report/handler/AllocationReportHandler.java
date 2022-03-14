package br.com.sw2you.realmeet.report.handler;

import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.email.TemplateType;
import br.com.sw2you.realmeet.report.enumeration.ReportHandlerType;
import br.com.sw2you.realmeet.report.model.AllocationReportData;
import br.com.sw2you.realmeet.report.validator.AbstractReportValidator;
import br.com.sw2you.realmeet.report.validator.AllocationReportValidator;
import br.com.sw2you.realmeet.util.Constants;
import br.com.sw2you.realmeet.util.DateUtils;
import java.time.OffsetTime;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AllocationReportHandler extends AbstractReportHandler<Allocation, AllocationReportData> {
    private final String PARAM_DATE_FROM = "DateFromFilter";
    private final String PARAM_DATE_TO = "DateToFilter";
    private final String FIELD_DATE_FROM = "DateFrom";
    private final String FIELD_DATE_TO = "DateTo";
    private final String FIELD_ROOM_NAME = "RoomName";
    private final String FIELD_EMPLOYEE_NAME = "EmployeeName";

    private final AllocationRepository allocationRepository;

    private final AllocationReportValidator allocationReportValidator;

    public AllocationReportHandler(
        @Qualifier("allocationReport") JasperReport jasperReport,
        AllocationRepository allocationRepository,
        AllocationReportValidator allocationReportValidator
    ) {
        super(jasperReport);
        this.allocationRepository = allocationRepository;
        this.allocationReportValidator = allocationReportValidator;
    }

    @Override
    protected void fillReportParams(HashMap<String, Object> reportParams, AllocationReportData reportData) {
        reportParams.put(PARAM_DATE_FROM, DateUtils.formatUsingPattern(reportData.getDateFrom()));
        reportParams.put(PARAM_DATE_TO, DateUtils.formatUsingPattern(reportData.getDateTo()));
    }

    @Override
    public TemplateType getTemplateType() {
        return TemplateType.ALLOCATION_REPORT;
    }

    @Override
    public AbstractReportValidator getReportValidator() {
        return allocationReportValidator;
    }

    @Override
    public ReportHandlerType getReportHandlerType() {
        return ReportHandlerType.ALLOCATION;
    }

    @Override
    protected List<Allocation> fetchReportData(AllocationReportData reportData) {
        return allocationRepository.findAllWithFilters(
            null,
            null,
            reportData.getDateFrom().atTime(OffsetTime.MIN),
            reportData.getDateTo().atTime(OffsetTime.MAX)
        );
    }

    @Override
    protected BiFunction<JRField, Allocation, Object> fieldMapperFunction() {
        return (
            (JRField jrField, Allocation allocation) -> {
                switch (jrField.getName()) {
                    case FIELD_DATE_FROM:
                        return DateUtils.formatUsingPattern(allocation.getStartAt());
                    case FIELD_DATE_TO:
                        return DateUtils.formatUsingPattern(allocation.getEndAt());
                    case FIELD_ROOM_NAME:
                        return allocation.getRoom().getName();
                    case FIELD_EMPLOYEE_NAME:
                        return allocation.getEmployee().getName();
                    default:
                        return Constants.EMPTY;
                }
            }
        );
    }
}

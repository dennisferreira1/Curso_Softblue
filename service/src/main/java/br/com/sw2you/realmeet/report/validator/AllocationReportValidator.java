package br.com.sw2you.realmeet.report.validator;

import br.com.sw2you.realmeet.report.model.AbstractReportData;
import br.com.sw2you.realmeet.report.model.AllocationReportData;
import br.com.sw2you.realmeet.util.Constants;
import br.com.sw2you.realmeet.validator.ValidationErrors;
import br.com.sw2you.realmeet.validator.ValidatorConstants;
import br.com.sw2you.realmeet.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AllocationReportValidator extends AbstractReportValidator {
    private final int maxMonthsInterval;

    public AllocationReportValidator(@Value(Constants.ALLOCATION_REPORT_MAX_MONTHS_INTERVAL) int maxMonthsInterval) {
        this.maxMonthsInterval = maxMonthsInterval;
    }

    @Override
    protected void validate(AbstractReportData reportData, ValidationErrors validationErrors) {
        var allocationReportData = (AllocationReportData) reportData;

        ValidatorUtils.validateRequired(
            allocationReportData.getDateFrom(),
            ValidatorConstants.DATE_FROM,
            validationErrors
        );

        ValidatorUtils.validateRequired(allocationReportData.getDateTo(), ValidatorConstants.DATE_TO, validationErrors);

        if (!validationErrors.hasErrors()) {
            ValidatorUtils.validateDatesOrdering(
                allocationReportData.getDateFrom(),
                allocationReportData.getDateTo(),
                validationErrors
            );

            ValidatorUtils.validateDurationBetweenDates(
                allocationReportData.getDateFrom(),
                allocationReportData.getDateTo(),
                maxMonthsInterval,
                validationErrors
            );
        }
    }
}

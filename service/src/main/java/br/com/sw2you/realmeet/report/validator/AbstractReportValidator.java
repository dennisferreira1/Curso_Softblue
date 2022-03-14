package br.com.sw2you.realmeet.report.validator;

import br.com.sw2you.realmeet.report.model.AbstractReportData;
import br.com.sw2you.realmeet.validator.ValidationErrors;
import br.com.sw2you.realmeet.validator.ValidatorConstants;
import br.com.sw2you.realmeet.validator.ValidatorUtils;

public abstract class AbstractReportValidator {

    public void validate(AbstractReportData abstractReportData) {
        var validationErrors = new ValidationErrors();

        ValidatorUtils.validateRequired(abstractReportData.getEmail(), ValidatorConstants.EMAIL, validationErrors);
        validate(abstractReportData, validationErrors);

        ValidatorUtils.throwOnError(validationErrors);
    }

    protected abstract void validate(AbstractReportData abstractReportData, ValidationErrors validationErrors);
}

package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.report.model.AllocationReportData;
import br.com.sw2you.realmeet.report.validator.AllocationReportValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import br.com.sw2you.realmeet.validator.ValidatorConstants;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllocationReportValidatorUnitTest extends BaseUnitTest {
    private final int MAX_MONTHS_INTERVAL = 12;
    private final LocalDate PARAM_DATE_FROM_FILTER = LocalDate.of(2022, 1, 1);
    private final LocalDate PARAM_DATE_TO_FILTER = LocalDate.of(2022, 5, 1);
    private final String EMAIL = "teste@teste.com";

    private AllocationReportValidator victim;
    private AllocationReportData.AllocationReportDataBuilder allocationReportData;

    @BeforeEach
    void setupEach() {
        victim = new AllocationReportValidator(MAX_MONTHS_INTERVAL);
        allocationReportData =
            AllocationReportData
                .newAllocationDataReportBuilder()
                .dateFrom(PARAM_DATE_FROM_FILTER)
                .dateTo(PARAM_DATE_TO_FILTER)
                .email(EMAIL);
    }

    @Test
    void testValidateWhenReportDataIsValid() {
        victim.validate(allocationReportData.build());
    }

    @Test
    void testValidateWhenEmailIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(allocationReportData.email(null).build())
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ValidatorConstants.EMAIL, ValidatorConstants.EMAIL + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateFromIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(allocationReportData.dateFrom(null).build())
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ValidatorConstants.DATE_FROM, ValidatorConstants.DATE_FROM + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateToIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(allocationReportData.dateTo(null).build())
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ValidatorConstants.DATE_TO, ValidatorConstants.DATE_TO + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateAreInverted() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    allocationReportData.dateFrom(PARAM_DATE_TO_FILTER).dateTo(PARAM_DATE_FROM_FILTER).build()
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ValidatorConstants.DATE_FROM, ValidatorConstants.DATE_FROM + INCONSISTENT),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDatesExceedsMaxInterval() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    allocationReportData.dateTo(PARAM_DATE_TO_FILTER.plusMonths(MAX_MONTHS_INTERVAL + 1)).build()
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ValidatorConstants.DATE_TO, ValidatorConstants.DATE_TO + EXCEEDS_MAX_INTERVAL),
            exception.getValidationErrors().getError(0)
        );
    }
}

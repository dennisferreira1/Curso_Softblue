package br.com.sw2you.realmeet.unit;

import br.com.sw2you.realmeet.config.JasperReportConfiguration;
import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.report.enumeration.ReportFormat;
import br.com.sw2you.realmeet.report.handler.AllocationReportHandler;
import br.com.sw2you.realmeet.report.resolver.ReportHandlerResolver;
import br.com.sw2you.realmeet.report.validator.AllocationReportValidator;
import br.com.sw2you.realmeet.service.ReportCreationService;
import br.com.sw2you.realmeet.service.ReportDispatcherService;
import br.com.sw2you.realmeet.util.Constants;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.validator.ValidationError;
import br.com.sw2you.realmeet.validator.ValidatorConstants;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;

class AllocationReportCreationServiceUnitTest extends BaseUnitTest {
    private final int MAX_MONTHS_INTERVAL = 12;

    private ReportCreationService victim;

    @Mock
    private ReportHandlerResolver reportHandlerResolver;

    @Mock
    private ReportDispatcherService reportDispatcherService;

    @Mock
    private AllocationRepository allocationRepository;

    @Mock
    private AllocationReportValidator allocationReportValidator;

    @BeforeEach
    void setupEach() {
        victim = new ReportCreationService(reportHandlerResolver, reportDispatcherService);
        BDDMockito
            .given(reportHandlerResolver.resolveReportHandler(ArgumentMatchers.any()))
            .willReturn(
                new AllocationReportHandler(
                    new JasperReportConfiguration().allocationReport(),
                    allocationRepository,
                    new AllocationReportValidator(MAX_MONTHS_INTERVAL)
                )
            );
    }

    @Test
    void testCreateAllocationReportSuccess() {
        victim.createAllocationReport(
            LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 12, 31),
            ConstantsTest.EMAIL_TO,
            ReportFormat.PDF.name()
        );

        Mockito.verify(reportDispatcherService).dispatch(ArgumentMatchers.any());
    }

    @Test
    void testCreateAllocationReportNoEmail() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.createAllocationReport(
                    LocalDate.of(2022, 1, 1),
                    LocalDate.of(2022, 12, 31),
                    Constants.EMPTY,
                    ReportFormat.PDF.name()
                )
        );

        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ValidatorConstants.EMAIL, ValidatorConstants.EMAIL + ValidatorConstants.MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testCreateAllocationReportDateFromAfterDateTo() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.createAllocationReport(
                    LocalDate.now(),
                    LocalDate.now().minusDays(1),
                    ConstantsTest.EMAIL_TO,
                    ReportFormat.PDF.name()
                )
        );

        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(
                ValidatorConstants.DATE_FROM,
                ValidatorConstants.DATE_FROM + ValidatorConstants.INCONSISTENT
            ),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testCreateAllocationReportDatesIntervalExceedsMax() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.createAllocationReport(
                    LocalDate.now(),
                    LocalDate.now().plusMonths(MAX_MONTHS_INTERVAL + 1),
                    ConstantsTest.EMAIL_TO,
                    ReportFormat.PDF.name()
                )
        );

        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(
                ValidatorConstants.DATE_TO,
                ValidatorConstants.DATE_TO + ValidatorConstants.EXCEEDS_MAX_INTERVAL
            ),
            exception.getValidationErrors().getError(0)
        );
    }
}

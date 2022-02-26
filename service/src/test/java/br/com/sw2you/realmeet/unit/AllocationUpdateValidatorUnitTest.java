package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class AllocationUpdateValidatorUnitTest extends BaseUnitTest {
    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    void setupEach() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    void testValidateWhenAllocationIsValid() {
        victim.validate(
            ConstantsTest.DEFAULT_ALLOCATION_ID,
            ConstantsTest.DEFAULT_ROOM_ID,
            TestDataCreator.newUpdateAllocationDTO()
        );
    }

    @Test
    void testValidateWhenIdIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(null, null, TestDataCreator.newUpdateAllocationDTO())
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ALLOCATION_ID, ALLOCATION_ID + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenSubjectIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    ConstantsTest.DEFAULT_ALLOCATION_ID,
                    ConstantsTest.DEFAULT_ROOM_ID,
                    TestDataCreator.newUpdateAllocationDTO().subject(null)
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenSubjectExceedsMaxLength() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    ConstantsTest.DEFAULT_ALLOCATION_ID,
                    ConstantsTest.DEFAULT_ROOM_ID,
                    TestDataCreator
                        .newUpdateAllocationDTO()
                        .subject(
                            StringUtils.rightPad(
                                ConstantsTest.DEFAULT_ALLOCATION_SUBJECT,
                                ALLOCATION_SUBJECT_MAX_LENGTH + 1,
                                'A'
                            )
                        )
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenStartAtIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    ConstantsTest.DEFAULT_ALLOCATION_ID,
                    ConstantsTest.DEFAULT_ROOM_ID,
                    TestDataCreator.newUpdateAllocationDTO().startAt(null)
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenEndAtIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    ConstantsTest.DEFAULT_ALLOCATION_ID,
                    ConstantsTest.DEFAULT_ROOM_ID,
                    TestDataCreator.newUpdateAllocationDTO().endAt(null)
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateOrderIsInvalid() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    ConstantsTest.DEFAULT_ALLOCATION_ID,
                    ConstantsTest.DEFAULT_ROOM_ID,
                    TestDataCreator
                        .newUpdateAllocationDTO()
                        .startAt(DateUtils.now().plusDays(1))
                        .endAt(DateUtils.now().plusDays(1).minusMinutes(30))
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + INCONSISTENT),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateIsInThePast() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    ConstantsTest.DEFAULT_ALLOCATION_ID,
                    ConstantsTest.DEFAULT_ROOM_ID,
                    TestDataCreator
                        .newUpdateAllocationDTO()
                        .startAt(DateUtils.now().minusHours(1))
                        .endAt(DateUtils.now())
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + IN_THE_PAST),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateIntervalExceedsMaxDuration() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    ConstantsTest.DEFAULT_ALLOCATION_ID,
                    ConstantsTest.DEFAULT_ROOM_ID,
                    TestDataCreator
                        .newUpdateAllocationDTO()
                        .startAt(DateUtils.now().plusDays(1))
                        .endAt(DateUtils.now().plusDays(1).plusSeconds(ALLOCATION_DURATION_MAX_SECONDS + 1))
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + EXCEEDS_MAX_DURATION),
            exception.getValidationErrors().getError(0)
        );
    }
}

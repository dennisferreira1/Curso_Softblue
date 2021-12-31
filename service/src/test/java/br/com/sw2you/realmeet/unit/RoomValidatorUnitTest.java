package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import br.com.sw2you.realmeet.validator.RoomValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class RoomValidatorUnitTest extends BaseUnitTest {
    private RoomValidator victim;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setupEach() {
        victim = new RoomValidator(roomRepository);
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(TestDataCreator.newCreateRoomDTO());
    }

    @Test
    void testValidateWhenRoomNameIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO().name(null))
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomNameExceedsMaxLength() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    TestDataCreator
                        .newCreateRoomDTO()
                        .name(StringUtils.rightPad(ConstantsTest.DEFAULT_ROOM_NAME, ROOM_NAME_MAX_LENGTH + 1, 'A'))
                )
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO().seats(null))
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + MISSIN),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreBelowThanMinValue() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO().seats(ROOM_SEATS_MIN_VALUE - 1))
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + BELOW_MIN_VALUE),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreGreaterThanMaxValue() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO().seats(ROOM_SEATS_MAX_VALUE + 1))
        );
        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + EXCEEDS_MAX_VALUE),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testeValidateWhenRoomNameIsDuplicate() {
        Mockito
            .when(roomRepository.findByNameAndActive(ConstantsTest.DEFAULT_ROOM_NAME, true))
            .thenReturn(Optional.of(TestDataCreator.newRoomBuilderDefault().build()));

        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO())
        );

        Assertions.assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        Assertions.assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + DUPLICATE),
            exception.getValidationErrors().getError(0)
        );
    }
}

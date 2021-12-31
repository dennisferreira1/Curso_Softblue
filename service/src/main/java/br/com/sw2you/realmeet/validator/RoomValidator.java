package br.com.sw2you.realmeet.validator;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {
    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO createRoomDTO) {
        ValidationErrors validationErrors = new ValidationErrors();

        if (
            validateName(createRoomDTO.getName(), validationErrors) &&
            validateSeats(createRoomDTO.getSeats(), validationErrors)
        ) {
            validateNameDuplicate(createRoomDTO.getName(), validationErrors);
        }

        ValidatorUtils.throwOnError(validationErrors);
    }

    private boolean validateName(String name, ValidationErrors validationErrors) {
        return (
            ValidatorUtils.validateRequired(name, ROOM_NAME, validationErrors) &&
            ValidatorUtils.validateMaxLength(name, ROOM_NAME, ROOM_NAME_MAX_LENGTH, validationErrors)
        );
    }

    private boolean validateSeats(Integer seats, ValidationErrors validationErrors) {
        return (
            ValidatorUtils.validateRequired(seats, ROOM_SEATS, validationErrors) &&
            ValidatorUtils.validateMaxValue(seats, ROOM_SEATS, ROOM_SEATS_MAX_VALUE, validationErrors) &&
            ValidatorUtils.validateMinValue(seats, ROOM_SEATS, ROOM_SEATS_MIN_VALUE, validationErrors)
        );
    }

    public void validateNameDuplicate(String name, ValidationErrors validationErrors) {
        roomRepository
            .findByNameAndActive(name, true)
            .ifPresent(__ -> validationErrors.add(ROOM_NAME, ROOM_NAME + DUPLICATE));
    }
}

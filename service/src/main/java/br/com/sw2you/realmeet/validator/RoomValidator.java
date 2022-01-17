package br.com.sw2you.realmeet.validator;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import java.util.Objects;
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
            validateNameDuplicate(null, createRoomDTO.getName(), validationErrors);
        }

        ValidatorUtils.throwOnError(validationErrors);
    }

    public void validate(Long id, UpdateRoomDTO updateRoomDTO) {
        ValidationErrors validationErrors = new ValidationErrors();

        if (
            ValidatorUtils.validateRequired(id, ROOM_ID, validationErrors) &&
            validateName(updateRoomDTO.getName(), validationErrors) &&
            validateSeats(updateRoomDTO.getSeats(), validationErrors)
        ) {
            validateNameDuplicate(id, updateRoomDTO.getName(), validationErrors);
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

    public void validateNameDuplicate(Long id, String name, ValidationErrors validationErrors) {
        roomRepository
            .findByNameAndActive(name, true)
            .ifPresent(
                room -> {
                    if (Objects.isNull(id) || !Objects.equals(id, room.getId())) {
                        validationErrors.add(ROOM_NAME, ROOM_NAME + DUPLICATE);
                    }
                }
            );
    }
}

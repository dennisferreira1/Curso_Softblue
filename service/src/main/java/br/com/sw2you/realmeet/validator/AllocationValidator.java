package br.com.sw2you.realmeet.validator;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;

import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.util.PageUtils;
import java.time.OffsetDateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class AllocationValidator {
    private final AllocationRepository allocationRepository;

    public AllocationValidator(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public void validate(CreateAllocationDTO createAllocationDTO) {
        ValidationErrors validationErrors = new ValidationErrors();

        validateSubject(createAllocationDTO.getSubject(), validationErrors);
        validateEmployeeName(createAllocationDTO.getEmployeeName(), validationErrors);
        validateEmployeeEmail(createAllocationDTO.getEmployeeEmail(), validationErrors);
        validateDates(
            createAllocationDTO.getRoomId(),
            createAllocationDTO.getStartAt(),
            createAllocationDTO.getEndAt(),
            validationErrors
        );

        ValidatorUtils.throwOnError(validationErrors);
    }

    public void validate(Long allocationId, Long roomId, UpdateAllocationDTO updateAllocationDTO) {
        ValidationErrors validationErrors = new ValidationErrors();

        if (ValidatorUtils.validateRequired(allocationId, ALLOCATION_ID, validationErrors)) {
            validateSubject(updateAllocationDTO.getSubject(), validationErrors);
            validateDates(roomId, updateAllocationDTO.getStartAt(), updateAllocationDTO.getEndAt(), validationErrors);
        }

        ValidatorUtils.throwOnError(validationErrors);
    }

    private void validateSubject(String subject, ValidationErrors validationErrors) {
        ValidatorUtils.validateRequired(subject, ALLOCATION_SUBJECT, validationErrors);
        ValidatorUtils.validateMaxLength(subject, ALLOCATION_SUBJECT, ALLOCATION_SUBJECT_MAX_LENGTH, validationErrors);
    }

    private void validateEmployeeName(String employeeName, ValidationErrors validationErrors) {
        ValidatorUtils.validateRequired(employeeName, ALLOCATION_EMPLOYEE_NAME, validationErrors);
        ValidatorUtils.validateMaxLength(
            employeeName,
            ALLOCATION_EMPLOYEE_NAME,
            ALLOCATION_EMPLOYEE_NAME_MAX_LENGTH,
            validationErrors
        );
    }

    private void validateEmployeeEmail(String employeeEmail, ValidationErrors validationErrors) {
        ValidatorUtils.validateRequired(employeeEmail, ALLOCATION_EMPLOYEE_EMAIL, validationErrors);
        ValidatorUtils.validateMaxLength(
            employeeEmail,
            ALLOCATION_EMPLOYEE_EMAIL,
            ALLOCATION_EMPLOYEE_EMAIL_MAX_LENGTH,
            validationErrors
        );
    }

    private void validateDates(
        Long roomId,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        ValidationErrors validationErrors
    ) {
        if (validateDatesPresent(startAt, endAt, validationErrors)) {
            ValidatorUtils.validateDatesOrdering(startAt, endAt, validationErrors);
            ValidatorUtils.validateDateInTheFuture(startAt, validationErrors);
            ValidatorUtils.validateDurationBetweenDates(startAt, endAt, validationErrors);
            validateIfTimeAvailable(roomId, startAt, endAt, validationErrors);
        }
    }

    private boolean validateDatesPresent(
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        ValidationErrors validationErrors
    ) {
        return (
            ValidatorUtils.validateRequired(startAt, ALLOCATION_START_AT, validationErrors) &&
            ValidatorUtils.validateRequired(endAt, ALLOCATION_END_AT, validationErrors)
        );
    }

    private void validateIfTimeAvailable(
        Long roomId,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        ValidationErrors validationErrors
    ) {
        var allocations = this.allocationRepository.findAllWithFilters(null, roomId, DateUtils.now(), endAt);

        allocations
            .stream()
            .filter(a -> DateUtils.isOverlapping(startAt, endAt, a.getStartAt(), a.getEndAt()))
            .findFirst()
            .ifPresent(__ -> validationErrors.add(ALLOCATION_DATES, ALLOCATION_DATES + OVERLAPPING));
    }
}

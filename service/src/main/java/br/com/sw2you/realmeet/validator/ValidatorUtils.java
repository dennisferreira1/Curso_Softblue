package br.com.sw2you.realmeet.validator;

import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.util.DateUtils;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public final class ValidatorUtils {

    private ValidatorUtils() {}

    public static void throwOnError(ValidationErrors validationErrors) {
        if (validationErrors.hasErrors()) {
            throw new InvalidRequestException(validationErrors);
        }
    }

    public static boolean validateRequired(String field, String fieldName, ValidationErrors validationErrors) {
        if (StringUtils.isBlank(field)) {
            validationErrors.add(fieldName, fieldName + ValidatorConstants.MISSIN);
            return false;
        }
        return true;
    }

    public static boolean validateRequired(Object field, String fieldName, ValidationErrors validationErrors) {
        if (Objects.isNull(field)) {
            validationErrors.add(fieldName, fieldName + ValidatorConstants.MISSIN);
            return false;
        }
        return true;
    }

    public static boolean validateMaxLength(
        String field,
        String fieldName,
        int maxLength,
        ValidationErrors validationErrors
    ) {
        if (!StringUtils.isBlank(field) && field.trim().length() > maxLength) {
            validationErrors.add(fieldName, fieldName + ValidatorConstants.EXCEEDS_MAX_LENGTH);
            return false;
        }
        return true;
    }

    public static boolean validateMaxValue(
        Integer field,
        String fieldName,
        int maxValue,
        ValidationErrors validationErrors
    ) {
        if (!Objects.isNull(field) && field > maxValue) {
            validationErrors.add(fieldName, fieldName + ValidatorConstants.EXCEEDS_MAX_VALUE);
            return false;
        }
        return true;
    }

    public static boolean validateMinValue(
        Integer field,
        String fieldName,
        int minValue,
        ValidationErrors validationErrors
    ) {
        if (!Objects.isNull(field) && field < minValue) {
            validationErrors.add(fieldName, fieldName + ValidatorConstants.BELOW_MIN_VALUE);
            return false;
        }
        return true;
    }

    public static void validateDatesOrdering(
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        ValidationErrors validationErrors
    ) {
        if (startAt.isEqual(endAt) || startAt.isAfter(endAt)) {
            validationErrors.add(
                ValidatorConstants.ALLOCATION_START_AT,
                ValidatorConstants.ALLOCATION_START_AT + ValidatorConstants.INCONSISTENT
            );
        }
    }

    public static void validateDateInTheFuture(OffsetDateTime startAt, ValidationErrors validationErrors) {
        if (startAt.isBefore(DateUtils.now())) {
            validationErrors.add(
                ValidatorConstants.ALLOCATION_START_AT,
                ValidatorConstants.ALLOCATION_START_AT + ValidatorConstants.IN_THE_PAST
            );
        }
    }

    public static void validateDurationBetweenDates(
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        ValidationErrors validationErrors
    ) {
        if (Duration.between(startAt, endAt).getSeconds() > ValidatorConstants.ALLOCATION_DURATION_MAX_SECONDS) {
            validationErrors.add(
                ValidatorConstants.ALLOCATION_END_AT,
                ValidatorConstants.ALLOCATION_END_AT + ValidatorConstants.EXCEEDS_MAX_DURATION
            );
        }
    }
}

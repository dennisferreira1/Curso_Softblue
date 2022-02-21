package br.com.sw2you.realmeet.exception;

import br.com.sw2you.realmeet.validator.ValidationErrors;
import br.com.sw2you.realmeet.validator.ValidatorConstants;

public class AllocationCannotBeDeletedException extends InvalidRequestException {

    public AllocationCannotBeDeletedException(long allocationId) {
        super(
            new ValidationErrors()
            .add(ValidatorConstants.ALLOCATION_ID, ValidatorConstants.ALLOCATION_ID + ValidatorConstants.IN_THE_PAST)
        );
    }
}

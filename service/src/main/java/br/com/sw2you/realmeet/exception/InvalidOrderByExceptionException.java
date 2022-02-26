package br.com.sw2you.realmeet.exception;

import br.com.sw2you.realmeet.validator.ValidationErrors;
import br.com.sw2you.realmeet.validator.ValidatorConstants;

public class InvalidOrderByExceptionException extends InvalidRequestException {

    public InvalidOrderByExceptionException() {
        super(
            new ValidationErrors()
            .add(ValidatorConstants.ORDER_BY, ValidatorConstants.ORDER_BY + ValidatorConstants.INVALID)
        );
    }
}

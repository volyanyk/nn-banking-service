package com.example.customer.handler;

import com.example.api.handler.ErrorDTO;
import com.example.api.handler.GlobalExceptionHandler;
import com.example.common.domain.exception.DomainException;
import com.example.customer.exception.IncorrectCustomerInputDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CustomerGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {IncorrectCustomerInputDataException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleException(IncorrectCustomerInputDataException incorrectCustomerInputDataException) {
        log.error(incorrectCustomerInputDataException.getMessage(), incorrectCustomerInputDataException);
        return new ErrorDTO(HttpStatus.NOT_FOUND.getReasonPhrase(), incorrectCustomerInputDataException.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = {DomainException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleException(DomainException domainException) {
        log.error(domainException.getMessage(), domainException);
        return new ErrorDTO(HttpStatus.NOT_FOUND.getReasonPhrase(), domainException.getMessage());
    }
}

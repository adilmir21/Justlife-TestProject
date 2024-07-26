package com.example.justlife.exceptions;

import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<Void> handleException(Exception exception) {
        log.error(Constants.EXCEPTION + "{}", exception.getMessage());
        return new ResponseDTO<>(null,
                Constants.STATUS_SERVER_ERROR, "Exception occurred: " + exception.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<Void> handleRuntimeException(RuntimeException exception) {
        log.error(Constants.EXCEPTION + "{}", exception.getMessage());
        return new ResponseDTO<>(null,
                Constants.STATUS_SERVER_ERROR, "Exception occurred: " + exception.getMessage());
    }
    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<Void> handleGenericException(GenericException exception) {
        log.error(Constants.EXCEPTION + "{}", exception.getMessage());
        return new ResponseDTO<>(null,
                Constants.STATUS_SERVER_ERROR, "An error has occurred: " + exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO<Void> handleBadRequestException(BadRequestException exception){
        log.error(Constants.EXCEPTION + "{}", exception.getMessage());
        return new ResponseDTO<>(null,
                Constants.STATUS_BAD_REQUEST,exception.getMessage());
    }

    @ExceptionHandler(InvalidArgumentsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO<Void> handleInvalidArgumentsException(InvalidArgumentsException exception){
        log.error(Constants.EXCEPTION + "{}", exception.getMessage());
        return new ResponseDTO<>(null,
                Constants.STATUS_BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO<Void> handleUserNotFoundException(ResourceNotFoundException exception){
        log.error(Constants.EXCEPTION + "{}", exception.getMessage());
        return new ResponseDTO<>(null,
                Constants.STATUS_NOT_FOUND, exception.getMessage());
    }
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDTO<Void> handleAlreadyExistsException(AlreadyExistsException exception){
        log.error(Constants.EXCEPTION + "{}", exception.getMessage());
        return new ResponseDTO<>(null, Constants.STATUS_ALREADY_EXISTS, exception.getMessage());
    }
}

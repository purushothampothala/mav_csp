package com.maveric.csp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.maveric.csp.constants.Constants.*;

@RestController
@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleInvalidException(MethodArgumentNotValidException ex){
        Map<String,String> errorMessage= new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->
            errorMessage.put(error.getField(),error.getDefaultMessage())
        );

        return errorMessage;
    }


    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException() {
        ApiError error = new ApiError(CUSTOMER_NOT_FOUND,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException() {
        ApiError error = new ApiError(PAGE_SIZE_EXCEPTION,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RequiredFieldsException.class)
    public ResponseEntity<ApiError> handleRequiredFieldsException() {
        ApiError error = new ApiError(MANDATORY_FIELDS_EXCEPTION,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = SessionNotFoundException.class)
    public ResponseEntity<ApiError> handleSessionNotFoundException() {
        ApiError error = new ApiError(SESSION_NOT_FOUND_EXCEPTION, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ArchivedSessionException.class)
    public ResponseEntity<ApiError> handleArchivedSessionException() {
        ApiError error = new ApiError(ARCHIVE_SESSION_EXCEPTION, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= InvalidCredentialsException.class)
    public ResponseEntity<ApiError>handleInvalidCredentialsException(){
        ApiError error = new ApiError(INVALID_CREDENTIALS,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value=StatusNotFoundException.class)
    public ResponseEntity<ApiError> handlingStatusNotFoundException(){
        ApiError error =new ApiError(STATUS_NOT_FOUND_EXCEPTION,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value=SessionAlreadyDeleted.class)
    public ResponseEntity<ApiError> handlingSessionAlreadyException(){
        ApiError error =new ApiError(ALREADY_DELETED_EXCEPTION,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}

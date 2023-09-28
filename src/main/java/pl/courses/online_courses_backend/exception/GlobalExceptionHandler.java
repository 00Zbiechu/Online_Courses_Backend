package pl.courses.online_courses_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.exception.model.ErrorDTO;
import pl.courses.online_courses_backend.exception.wrapper.ErrorList;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ErrorList> handleCustomErrorException(CustomErrorException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder().field(ex.getField()).errorCodes(ex.getErrorCode()).build();
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorList.builder().errorList(List.of(errorDTO)).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorList> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorDTO> errorList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errorList.add(ErrorDTO.builder().field(fieldName).errorCodes(errorMessage).build());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorList.builder().errorList(errorList).build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorList> handleInternalAuthenticationServiceException() {

        ErrorList errorList = ErrorList.builder()
                .errorList(
                        List.of(
                                ErrorDTO.builder()
                                        .field("username")
                                        .errorCodes(ErrorCodes.WRONG_CREDENTIALS)
                                        .build(),
                                ErrorDTO.builder()
                                        .field("password")
                                        .errorCodes(ErrorCodes.WRONG_CREDENTIALS)
                                        .build()
                        )
                ).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorList);
    }
}
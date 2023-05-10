package pl.courses.online_courses_backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.courses.online_courses_backend.exception.wrapper.ErrorsDTO;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorsDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<ErrorDTO> errorList = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error ->
                        ErrorDTO.builder()
                                .message(buildErrorMessage(error))
                                .build()
                ).toList();


        return ResponseEntity.badRequest().body(ErrorsDTO.builder().errorList(errorList).build());

    }

    private String buildErrorMessage(ObjectError objectError) {

        return String.format("Field in object %s has error: %s", objectError.getObjectName(), objectError.getDefaultMessage());

    }

}
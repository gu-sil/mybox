package gusil.mybox.aop;

import gusil.mybox.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.ResponseEntity.notFound;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity postNotFound(UserNotFoundException ex) {
        log.debug("handling exception::" + ex);
        return notFound().build();
    }
}

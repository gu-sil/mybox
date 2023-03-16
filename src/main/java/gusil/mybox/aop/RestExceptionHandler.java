package gusil.mybox.aop;

import gusil.mybox.exception.DirectoryNotFoundException;
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
    ResponseEntity handleUserNotFound(UserNotFoundException ex) {
        log.debug("handling exception::" + ex);
        return notFound().build();
    }

    @ExceptionHandler(DirectoryNotFoundException.class)
    ResponseEntity handleDirectoryNotFound(DirectoryNotFoundException ex) {
        log.debug("handling exception::" + ex);
        return notFound().build();
    }
}

package gusil.mybox.aop;

import gusil.mybox.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(UserUsageExceedsException.class)
    ResponseEntity handleUserNotFound(UserUsageExceedsException ex) {
        log.debug("handling exception::" + ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(DirectoryNotFoundException.class)
    ResponseEntity handleDirectoryNotFound(DirectoryNotFoundException ex) {
        log.debug("handling exception::" + ex);
        return notFound().build();
    }
    @ExceptionHandler(DirectoryHasChildException.class)
    ResponseEntity handleDirectoryHasChild(DirectoryHasChildException ex) {
        log.debug("handling exception::" + ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(FileNotFoundException.class)
    ResponseEntity handleFileNotFound(FileNotFoundException ex) {
        log.debug("handling exception::" + ex);
        return notFound().build();
    }

    @ExceptionHandler(NameDuplicatedException.class)
    ResponseEntity handleNameDuplicated(NameDuplicatedException ex) {
        log.debug("handling exception::" + ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}

package raisetech.StudentManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<String> handleNotFound(StudentNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("受講生が見つかりません。" + ex.getMessage());
  }

}

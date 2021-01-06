package uk.gov.onsdigital.banner;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<Object> handleConflict(
        RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Unhandled exception has been caught";
    return handleExceptionInternal(ex, bodyOfResponse, 
      new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}
package test.snapp.box.feedback.application.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import test.snapp.box.feedback.application.advice.exceptions.AlreadyExistException;
import test.snapp.box.feedback.application.advice.exceptions.ForbiddenResourceException;
import test.snapp.box.feedback.application.advice.exceptions.NotFoundException;
import test.snapp.box.feedback.application.common.MessageResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<MessageResponse> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            sb.append(((FieldError) error).getField());
            sb.append(" ");
            sb.append(error.getDefaultMessage()).append("; ");
        });
        return new ResponseEntity<>(new MessageResponse(sb.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<MessageResponse> handleAlreadyExistException(AlreadyExistException ex) {
        return new ResponseEntity<>(new MessageResponse(ex.getDescription()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ForbiddenResourceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<MessageResponse> handleForbiddenResourceException(ForbiddenResourceException ex) {
        return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<MessageResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new MessageResponse(ex.getDescription()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<MessageResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}


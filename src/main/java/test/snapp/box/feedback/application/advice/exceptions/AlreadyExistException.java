package test.snapp.box.feedback.application.advice.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {
    private final String description;

    public AlreadyExistException(String entity) {
        this.description = String.format("%s already exist", entity);
    }
}

package test.snapp.box.feedback.application.advice.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String description;

    public NotFoundException(String entity) {
        this.description = String.format("%s not found", entity);
    }
}

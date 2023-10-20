package test.snapp.box.feedback.application.advice.exceptions;

import lombok.Getter;

@Getter
public class ForbiddenResourceException extends RuntimeException {
    private final String description;

    public ForbiddenResourceException(String entity, Long id) {
        this.description = String.format("Access denied for %s with id: %d", entity, id);
    }
}

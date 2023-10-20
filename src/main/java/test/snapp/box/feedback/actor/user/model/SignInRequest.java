package test.snapp.box.feedback.actor.user.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}

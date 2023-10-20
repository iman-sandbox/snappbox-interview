package test.snapp.box.feedback.actor.user;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import test.snapp.box.feedback.actor.user.model.JwtResponse;
import test.snapp.box.feedback.actor.user.model.SignInRequest;
import test.snapp.box.feedback.actor.user.model.SignUpRequest;
import test.snapp.box.feedback.application.common.MessageResponse;
import test.snapp.box.feedback.domain.user.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        JwtResponse jwtResponse = authService.signIn(signInRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}



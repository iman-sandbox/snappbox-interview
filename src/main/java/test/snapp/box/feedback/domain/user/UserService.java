package test.snapp.box.feedback.domain.user;

import org.springframework.stereotype.Service;
import test.snapp.box.feedback.application.advice.exceptions.NotFoundException;
import test.snapp.box.feedback.application.security.JwtUtils;
import test.snapp.box.feedback.domain.user.data.User;
import test.snapp.box.feedback.domain.user.data.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public User getUserByBearerToken(String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User")
        );
    }
}

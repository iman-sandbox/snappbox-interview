package test.snapp.box.feedback.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import test.snapp.box.feedback.actor.user.model.JwtResponse;
import test.snapp.box.feedback.actor.user.model.SignInRequest;
import test.snapp.box.feedback.actor.user.model.SignUpRequest;
import test.snapp.box.feedback.application.advice.exceptions.AlreadyExistException;
import test.snapp.box.feedback.application.advice.exceptions.NotFoundException;
import test.snapp.box.feedback.application.security.JwtUtils;
import test.snapp.box.feedback.domain.biker.data.Biker;
import test.snapp.box.feedback.domain.biker.data.BikerRepository;
import test.snapp.box.feedback.domain.customer.data.Customer;
import test.snapp.box.feedback.domain.customer.data.CustomerRepository;
import test.snapp.box.feedback.domain.user.data.Role;
import test.snapp.box.feedback.domain.user.data.User;
import test.snapp.box.feedback.domain.user.data.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final String ROLE_PREFIX = "ROLE_";
    private final UserRepository userRepository;
    private final BikerRepository bikerRepository;
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;


    public AuthService(
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            BikerRepository bikerRepository,
            CustomerRepository customerRepository, PasswordEncoder encoder) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.bikerRepository = bikerRepository;
        this.customerRepository = customerRepository;
        this.encoder = encoder;
    }

    public JwtResponse signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        return new JwtResponse(jwt);
    }

    public void signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername()))
            throw new AlreadyExistException("Username");
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        Role role;
        try {
            role = Role.valueOf(ROLE_PREFIX + signUpRequest.getRole());
        } catch (Throwable exception) {
            throw new NotFoundException("Role");
        }
        user.setRole(role.name());
        User persistedUser = userRepository.save(user);
        switch (role) {
            case ROLE_BIKER -> {
                Biker biker = new Biker();
                biker.setName(signUpRequest.getName());
                biker.setUserId(persistedUser.getId());
                bikerRepository.save(biker);
            }
            case ROLE_CUSTOMER -> {
                Customer customer = new Customer();
                customer.setName(signUpRequest.getName());
                customer.setUserId(persistedUser.getId());
                customerRepository.save(customer);
            }
            case ROLE_MANAGER -> {}
        }
    }
}



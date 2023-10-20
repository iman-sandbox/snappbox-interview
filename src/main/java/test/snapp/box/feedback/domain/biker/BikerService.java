package test.snapp.box.feedback.domain.biker;

import org.springframework.stereotype.Service;
import test.snapp.box.feedback.application.advice.exceptions.NotFoundException;
import test.snapp.box.feedback.domain.biker.data.Biker;
import test.snapp.box.feedback.domain.biker.data.BikerRepository;
import test.snapp.box.feedback.domain.user.UserService;

@Service
public class BikerService {
    private final BikerRepository bikerRepository;
    private final UserService userService;

    public BikerService(BikerRepository bikerRepository, UserService userService) {
        this.bikerRepository = bikerRepository;
        this.userService = userService;
    }

    public Biker getByBearerToken(String token) {
        return getByUserId(userService.getUserByBearerToken(token).getId());
    }

    private Biker getByUserId(Long userId) {
        return bikerRepository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException("Biker")
        );
    }
}

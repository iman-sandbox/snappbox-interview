package test.snapp.box.feedback.domain.customer;

import test.snapp.box.feedback.application.advice.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import test.snapp.box.feedback.domain.customer.data.Customer;
import test.snapp.box.feedback.domain.customer.data.CustomerRepository;
import test.snapp.box.feedback.domain.user.UserService;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    public CustomerService(CustomerRepository customerRepository, UserService userService) {
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    public Customer getByBearerToken(String token) {
        return getByUserId(userService.getUserByBearerToken(token).getId());
    }

    public Customer getByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Customer"));
    }
}

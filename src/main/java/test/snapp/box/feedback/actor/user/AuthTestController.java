package test.snapp.box.feedback.actor.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/test")
public class AuthTestController {

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('CUSTOMER') or hasRole('BIKER') or hasRole('MANAGER')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/customer")
  @PreAuthorize("hasRole('CUSTOMER')")
  public String customerAccess() {
    return "Customer Board.";
  }

  @GetMapping("/biker")
  @PreAuthorize("hasRole('BIKER')")
  public String bikerAccess() {
    return "Biker Board.";
  }

  @GetMapping("/manager")
  @PreAuthorize("hasRole('MANAGER')")
  public String managerAccess() {
    return "Manager Board.";
  }
}

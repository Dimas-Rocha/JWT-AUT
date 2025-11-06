package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@GetMapping("/all")
	public String allAccess() {
		logger.info("Public endpoint accessed");
		return "Public Content - Anyone can access this";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String userAccess() {
		logger.info("User endpoint accessed");
		return "User Content - Only authenticated users can access this";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		logger.info("Admin endpoint accessed");
		return "Admin Board - Only users with ADMIN role can access this";
	}

	@GetMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public String userProfile() {
		logger.info("Profile endpoint accessed");
		return "User Profile - Any authenticated user can access this";
	}
}
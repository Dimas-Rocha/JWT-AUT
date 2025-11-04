package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.JwtResponse;
import dto.LoginRequest;

import config.JwtTokenProvider;
import entity.User;
import repository.UserRepository;

@Service
@Transactional
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RabbitMQProducer rabbitMQProducer;

	public JwtResponse authenticateUser(LoginRequest loginRequest) {
		logger.info("Authenticating user: {}", loginRequest.getUsername());

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);

		User user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found: " + loginRequest.getUsername()));

		// Publicar evento de login no RabbitMQ
		rabbitMQProducer.sendLoginEvent(user.getUsername(), user.getRole());

		logger.info("User authenticated successfully: {}", user.getUsername());

		return new JwtResponse(jwt, user.getUsername(), user.getRole());
	}

	public User registerUser(LoginRequest signUpRequest) {
		logger.info("Registering new user: {}", signUpRequest.getUsername());

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new RuntimeException("Username is already taken: " + signUpRequest.getUsername());
		}

		User user = new User();
		user.setUsername(signUpRequest.getUsername());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		user.setRole("USER");

		User savedUser = userRepository.save(user);

		// Publicar evento de registro no RabbitMQ
		rabbitMQProducer.sendRegistrationEvent(savedUser.getUsername(), savedUser.getRole());

		logger.info("User registered successfully: {}", savedUser.getUsername());

		return savedUser;
	}

	public void logoutUser(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		// Publicar evento de logout no RabbitMQ
		rabbitMQProducer.sendLogoutEvent(user.getUsername(), user.getRole());

		logger.info("User logged out: {}", username);
	}
}
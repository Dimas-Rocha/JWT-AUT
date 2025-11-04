package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dto.UserEventMessage;

@Service
public class RabbitMQProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange.name}")
	private String exchange;

	@Value("${rabbitmq.routing.json.key}")
	private String routingJsonKey;

	public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendUserEvent(UserEventMessage userEvent) {
		LOGGER.info("Sending user event to RabbitMQ: {}", userEvent);
		try {
			rabbitTemplate.convertAndSend(exchange, routingJsonKey, userEvent);
			LOGGER.info("User event sent successfully: {}", userEvent.getEventType());
		} catch (Exception e) {
			LOGGER.error("Failed to send user event to RabbitMQ: {}", e.getMessage(), e);
		}
	}

	public void sendLoginEvent(String username, String role) {
		UserEventMessage event = new UserEventMessage("USER_LOGIN", username, role, "User logged in successfully");
		sendUserEvent(event);
	}

	public void sendRegistrationEvent(String username, String role) {
		UserEventMessage event = new UserEventMessage("USER_REGISTRATION", username, role, "New user registered");
		sendUserEvent(event);
	}

	public void sendLogoutEvent(String username, String role) {
		UserEventMessage event = new UserEventMessage("USER_LOGOUT", username, role, "User logged out");
		sendUserEvent(event);
	}
}

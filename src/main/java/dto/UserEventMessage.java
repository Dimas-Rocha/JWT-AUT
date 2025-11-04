package dto;

import java.time.LocalDateTime;

public class UserEventMessage {

	private String eventType;
	private String username;
	private String role;
	private LocalDateTime timestamp;
	private String description;
	
	public UserEventMessage() {
		this.timestamp = LocalDateTime.now();
	}

	public UserEventMessage(String eventType, String username, String role, String description) {
		
		this.eventType = eventType;
		this.username = username;
		this.role = role;
		this.description = description;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UserEventMessage [eventType=" + eventType + ", username=" + username + ", role=" + role + ", timestamp="
				+ timestamp + ", description=" + description + "]";
	}
	
	
	
	
	
}

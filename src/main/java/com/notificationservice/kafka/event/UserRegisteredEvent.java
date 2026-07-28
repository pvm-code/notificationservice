package com.notificationservice.kafka.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserRegisteredEvent {
	
	private UUID id;
	
	private String email;
	
	private String name;
	
	private LocalDateTime registeredAt;

	public UserRegisteredEvent(UUID id, String email, String name, LocalDateTime registeredAt) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.registeredAt = registeredAt;
	}

	public UserRegisteredEvent() {
		
	}
	@Override
	public String toString() {
	    return "UserRegisteredEvent{" +
	            "id=" + id +
	            ", email='" + email + '\'' +
	            ", name='" + name + '\'' +
	            ", registeredAt=" + registeredAt +
	            '}';
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(LocalDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}
	
	
	
	

}

package com.notificationservice.metrics;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class MetricsService {
	
	private final Counter emailFailure;
	private final Counter emailSuccess;
	private final Counter kafkaConsumed;
	private final Counter jwtValidationFailure;

	
	
	public MetricsService(MeterRegistry registry) {
			
		this.emailFailure=
				registry.counter("emails_failed_total");
		
		this.emailSuccess=
				registry.counter("emails_sent_total");
		
		
		this.kafkaConsumed=
				registry.counter("kafka_messages_consumed_total");
		

		this.jwtValidationFailure=
				registry.counter("jwt_validation_failed_total");
		
	}
	

    public void incrementEmailFailed() {
        emailFailure.increment();
    }

    public void incrementEmailSent() {
        emailSuccess.increment();
    }


    public void incrementKafkaConsumed() {
        kafkaConsumed.increment();
    }

    public void incrementJwtFailure() {
        jwtValidationFailure.increment();
    }

}

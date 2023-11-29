package com.banking.saga.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

	private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

	@Autowired
	private final NotificationRepository notificationRepository;

	public NotificationListener(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	@KafkaListener(topics = "notification_topic", groupId = "group-id")
	public void listenNotifications(String notificationStr) {
		try {
			logger.info("NotificationListener.listenNotifications.Received Message -> " + notificationStr);
			Notification notification= JacksonFactory.getObjectMapper().readValue(notificationStr, Notification.class);
			if (notification != null) {
				
				notificationRepository.save(notification);
				logger.info("Notification inserted successfully !!");
				} 
			else {
					logger.info("Notification object is null.");
				}
			}
			catch (Exception ex) {
				logger.info("Unhandled Exception" + ex.getMessage());
			}
	}
}
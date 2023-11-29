package com.banking.saga.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

	private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
	private static final String notification_topic = "notification_topic";

	@Autowired
	private final TransactionRepository transactionRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public TransactionListener(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@KafkaListener(topics = "trans-result-topic", groupId = "group-id")
	public void listenTransaction(String transactionPOJO) {
		try {
			logger.info("TransactionListener.listenTransaction.Recived Message -> " + transactionPOJO);
			Transaction transaction = JacksonFactory.getObjectMapper().readValue(transactionPOJO, Transaction.class);
			if (transaction != null) {
				Transaction transactionToBeUpdated = transactionRepository.findBytransactionId(transaction.getTransactionId());
				if(transaction.getStatus() == TransactionStatus.SUCCESS || transaction.getStatus() == TransactionStatus.FAILURE )
				{
					transactionToBeUpdated.setStatus(transaction.getStatus());
					transactionToBeUpdated.setTransactionNotes(transaction.getTransactionNotes());
					transactionRepository.save(transactionToBeUpdated);
					NotificationPOJO notificationSender = new NotificationPOJO(transaction.getTransactionId(),transaction.getSenderAccount(),transaction.getTransactionNotes());
					kafkaTemplate.send(notification_topic, JacksonFactory.getObjectMapper().writeValueAsString(notificationSender));
					NotificationPOJO notificationReceiver = new NotificationPOJO(transaction.getTransactionId(),transaction.getReceiverAccount(),transaction.getTransactionNotes());
					kafkaTemplate.send(notification_topic, JacksonFactory.getObjectMapper().writeValueAsString(notificationReceiver));
					logger.info("Transaction updated successfully !!");
				} else {
					logger.info("Unexcepted Transaction status.");
				}
			}
		} catch (Exception ex) {
				logger.info("Un Handled Exception" + ex.getMessage());
		}
	}
}

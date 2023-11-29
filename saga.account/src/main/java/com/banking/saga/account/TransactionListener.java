package com.banking.saga.account;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TransactionListener {

	private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
	private static final String trans_result_topic = "trans-result-topic";

	@Autowired
	private final AccountRepository accountRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public TransactionListener(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@KafkaListener(topics = "transaction-topic", groupId = "group-id")
	public void listenTransaction(String transactionPOJO) throws JsonMappingException, JsonProcessingException{
		logger.info("TransactionListener.listenTransaction.Received Message -> " + transactionPOJO);
		try {
			TransactionPOJO transaction = JacksonFactory.getObjectMapper().readValue(transactionPOJO, TransactionPOJO.class);
			if (transaction != null) {
				Optional<Account> senderAccount = accountRepository.findByaccountNumber(transaction.getSenderAccount());
				Optional<Account> receiverAccount = accountRepository.findByaccountNumber(transaction.getReceiverAccount());
				if (senderAccount != null && receiverAccount != null) {
					if (senderAccount.get().getBalance() >= transaction.getAmount()) {
						senderAccount.get().setBalance(senderAccount.get().getBalance() - transaction.getAmount());
						receiverAccount.get().setBalance(receiverAccount.get().getBalance() + transaction.getAmount());
						accountRepository.save(senderAccount.get());
						accountRepository.save(receiverAccount.get());
						//transaction.setTransactionNotes(String.format("Ammount has been transfered from sender(%s) to receier(%s) account. Transfered amount : $%d",senderAccount.get().getAccountNumber(),receiverAccount.get().getAccountNumber(),transaction.getAmount() ));
						transaction.setTransactionNotes("Ammount has been transfered from sender to receier account.");
						logger.info("Ammount has been transfered from sender to receier account.");
						transaction.setStatus(TransactionStatus.SUCCESS);

						kafkaTemplate.send(trans_result_topic,JacksonFactory.getObjectMapper().writeValueAsString(transaction));
					} else {
						transaction.setStatus(TransactionStatus.FAILURE);
						transaction.setTransactionNotes("Insufficient fund.");
						logger.info("Insufficient fund.");
						kafkaTemplate.send(trans_result_topic,JacksonFactory.getObjectMapper().writeValueAsString(transaction));
					}
				} else {
					transaction.setStatus(TransactionStatus.FAILURE);
					transaction.setTransactionNotes("Sender / Receiver account numer is not valid !");
					kafkaTemplate.send(trans_result_topic,JacksonFactory.getObjectMapper().writeValueAsString(transaction));
					logger.info("Sender / Receiver account numer is not valid !");
				}
			} else {
				TransactionPOJO errorPOJO = new TransactionPOJO();
				errorPOJO.setStatus(TransactionStatus.FAILURE);
				errorPOJO.setTransactionNotes("Transaction Object is null");
				kafkaTemplate.send(trans_result_topic,JacksonFactory.getObjectMapper().writeValueAsString(errorPOJO));
				logger.info("Transaction Object is null");
			}
		} catch (Exception ex) {
			if (transactionPOJO != null && !transactionPOJO.isEmpty()) {
				TransactionPOJO transaction = new ObjectMapper().readValue(transactionPOJO, TransactionPOJO.class);
				transaction.setStatus(TransactionStatus.FAILURE);
				transaction.setTransactionNotes("Un Handled Exception" + ex.getMessage());
				logger.info("Un Handled Exception" + ex.getMessage());
				kafkaTemplate.send(trans_result_topic,JacksonFactory.getObjectMapper().writeValueAsString(transaction));
			} else {
				TransactionPOJO errorPOJO = new TransactionPOJO();
				errorPOJO.setStatus(TransactionStatus.FAILURE);
				errorPOJO.setTransactionNotes("Transaction Object is null");
				kafkaTemplate.send(trans_result_topic,JacksonFactory.getObjectMapper().writeValueAsString(errorPOJO));
				logger.info("Transaction Object is null");
			}
		}
	}
}

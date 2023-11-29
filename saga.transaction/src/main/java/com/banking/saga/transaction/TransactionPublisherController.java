package com.banking.saga.transaction;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
	
@RestController
@RequestMapping("/TransactionPublisher")
public class TransactionPublisherController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    @Autowired
    private TransactionRepository transactionRepository;
    
    private String TOPIC = "transaction-topic"; 

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionPublisherController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> publishTransaction(@RequestBody Transaction transaction) {
    	try {
    	logger.info("Publishing the transaction !");
    	transaction.setTransactionId(transaction.generateTransactionId());
    	transaction.setTimestamp(LocalDateTime.now());
    	transaction.setStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);
    	kafkaTemplate.send(TOPIC, JacksonFactory.getObjectMapper().writeValueAsString(transaction));
		logger.info("TransactionPublisherController.publishTransaction.Publish Message -> " + JacksonFactory.getObjectMapper().writeValueAsString(transaction));
    	logger.info("Publishing the transaction completed.!");
    	 return ResponseEntity.ok("Transaction Published Sucessfully");
    	}catch(Exception ex)
    	{
        	logger.info("Publishing exception.!" + ex.getMessage());
        	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
}
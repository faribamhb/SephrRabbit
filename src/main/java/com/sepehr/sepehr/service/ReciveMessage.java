package com.sepehr.sepehr.service;

import com.sepehr.sepehr.entity.Person;
import com.sepehr.sepehr.repository.TestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ReciveMessage {
    private static final Logger logger = LoggerFactory.getLogger(ReciveMessage.class);
    private final TestRepository testRepository;

    public ReciveMessage(TestRepository testRepository) {
        this.testRepository = testRepository;
    }


    @RabbitListener(queues ="fariba1.queue")
    public void saveMessageInDatabase(Person test) throws IOException, ClassNotFoundException {
        logger.info("message received in queue is: " + test);
        logger.info("recive message");
        testRepository.save(test);
    }
}

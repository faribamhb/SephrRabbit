package com.sepehr.sepehr.service;

import com.sepehr.sepehr.common.exceptions.ErrorCodes;
import com.sepehr.sepehr.common.exceptions.ServiceException;
import com.sepehr.sepehr.entity.Person;
import com.sepehr.sepehr.repository.TestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;

@Service
public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);
    private final TestRepository testRepository;
    private final RabbitTemplate rabbitTemplate;


    public TestService(TestRepository testRepository, RabbitTemplate rabbitTemplate) {
        this.testRepository = testRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public String insert(Person test) {
        logger.info("go to mq");
        rabbitTemplate.convertAndSend("fariba1.exchange", "fariba1.routingkey", test);
        return "success";

    }

    public Person findById(Long personId) throws ServiceException {
        return   testRepository.findById(personId).map(post -> {
            post.setName("ali");
           return   testRepository.save(post);
        }) .orElseThrow(() -> new ServiceException("Cannot find the user in oauth", ErrorCodes.NO_ENTITY.getCode()));

    }
}

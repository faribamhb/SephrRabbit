package com.sepehr.sepehr.service;

import com.sepehr.sepehr.common.data.*;
import com.sepehr.sepehr.common.exceptions.ErrorCodes;
import com.sepehr.sepehr.common.exceptions.ServiceException;
import com.sepehr.sepehr.entity.Person;
import com.sepehr.sepehr.repository.SearchableSpecification;
import com.sepehr.sepehr.repository.TestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.util.List;

@Service
public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);
    private final TestRepository testRepository;
    private final RabbitTemplate rabbitTemplate;


    private Searchable[] searchables = new Searchable[]{
            new Searchable("name", "name", SearchableOperation.LIKE, SearchableType.STRING),

    };
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

    public Page<Person> showData(SearchablePage searchablePage) throws ServiceException {
        SearchablePage searchableResult = SearchUtils.getSearchableResult(searchablePage, searchables);


            return  testRepository.findAll(new SearchableSpecification<Person>(searchableResult.getSearchables()),
                    searchableResult.getPageRequest());

    }
}

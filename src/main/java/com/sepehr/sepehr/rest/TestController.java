package com.sepehr.sepehr.rest;

import com.sepehr.sepehr.common.data.SearchablePage;
import com.sepehr.sepehr.common.exceptions.ServiceException;
import com.sepehr.sepehr.entity.Person;
import com.sepehr.sepehr.entity.Request;
import com.sepehr.sepehr.entity.Test;
import com.sepehr.sepehr.service.TestService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/customHeader")
    ResponseEntity<String> customHeader() {
        Request request = new Request();
        request.setRequest("tafanoni");
        Request request2 = new Request();
        request2.setRequest("tafanoni2");
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        requests.add(request2);
        Person person = new Person();
        person.setName("fariba");
        person.setRequests(requests);
        return new ResponseEntity<>(testService.insert(person), HttpStatus.OK);
    }


    @PutMapping(value = "/updateUser/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> updatePost(@PathVariable Long personId) throws ServiceException {

        return new ResponseEntity<>(testService.findById(personId), HttpStatus.OK);

    }


    @GetMapping(value = "/showData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Person>> showData(@RequestParam int page,
                                                 @RequestParam int total,
                                                 @RequestParam String order,
                                                 @RequestParam String direction,
                                                 @RequestParam(required = false) String name

    ) throws ServiceException {
        Map<String, String> query = new HashMap<>();
        query.put("name", name);

        SearchablePage searchablePage = SearchablePage.builder()
                .page(page).order(order).total(total).direction(direction).filter(query).build();

        return new ResponseEntity<>(testService.showData(searchablePage), HttpStatus.OK);

    }

}

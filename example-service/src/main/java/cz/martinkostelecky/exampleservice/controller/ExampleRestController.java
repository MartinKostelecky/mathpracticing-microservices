package cz.martinkostelecky.exampleservice.controller;

import cz.martinkostelecky.exampleservice.entity.Example;
import cz.martinkostelecky.exampleservice.exception.ExampleNotFoundException;
import cz.martinkostelecky.exampleservice.service.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExampleRestController {

    private final Environment environment;
    private final ExampleService exampleService;

    //TODO produces = "application/json" ?
    @RequestMapping(value = "/get", produces = "application/json", method = RequestMethod.GET)
    public Example getExampleById(@RequestParam Long id) throws ExampleNotFoundException {

        log.info(environment.getProperty("local.server.port"));
        return exampleService.getExampleById(id);
    }

    //TODO produces = "application/json" ?
    @RequestMapping(value = "/getExample", produces = "application/json", method = RequestMethod.GET)
    public List<Example> getExamplesByCategory(@RequestParam String category) throws ExampleNotFoundException {

        log.info(environment.getProperty("local.server.port"));
        return exampleService.getExamplesByCategory(category);
    }

    @RequestMapping(value = "/getResult", produces = "application/json", method = POST)
    public Boolean getResult(@RequestBody Example example) {

        return exampleService.getResult(example);
    }
}

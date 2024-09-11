package cz.martinkostelecky.exampleservice.service;

import cz.martinkostelecky.exampleservice.entity.Example;
import cz.martinkostelecky.exampleservice.exception.ExampleAlreadyExistException;
import cz.martinkostelecky.exampleservice.exception.ExampleNotFoundException;

import java.util.List;

public interface ExampleService {

    List<Example> getAllExamples();

    void saveExample(Example example) throws ExampleAlreadyExistException;

    Example getExampleById(long id) throws ExampleNotFoundException;

    List<Example> getExamplesByCategory(String category) throws ExampleNotFoundException;

    void updateExample(Example example) throws ExampleNotFoundException, ExampleAlreadyExistException;

    void deleteExampleById(Long id);

    Boolean getResult(Example example);

}

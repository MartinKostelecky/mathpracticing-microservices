package cz.martinkostelecky.exampleservice.service.impl;

import cz.martinkostelecky.exampleservice.entity.Example;
import cz.martinkostelecky.exampleservice.exception.ExampleAlreadyExistException;
import cz.martinkostelecky.exampleservice.exception.ExampleNotFoundException;
import cz.martinkostelecky.exampleservice.repository.ExampleRepository;
import cz.martinkostelecky.exampleservice.service.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    @Override
    public List<Example> getAllExamples() {
        return exampleRepository.findAll();
    }

    public void saveExample(Example example) throws ExampleAlreadyExistException {
        Boolean existByExampleTitle = exampleRepository.existByExampleTitle(example.getExampleTitle());
        if (existByExampleTitle) {
            throw new ExampleAlreadyExistException("Example " + example.getExampleTitle() + " already exist in database.");
        }
        exampleRepository.save(example);
    }

    @Override
    public Example getExampleById(long id) throws ExampleNotFoundException {
        Optional<Example> example = exampleRepository.findById(id);

        return example.orElseThrow(() -> new ExampleNotFoundException("Example not found!"));

    }

    @Override
    public List<Example> getExamplesByCategory(String category) throws ExampleNotFoundException {
        Optional<List<Example>> optionalExamples = Optional.ofNullable(exampleRepository.findByCategory(category));
        return optionalExamples.orElseThrow(() -> new ExampleNotFoundException("Examples of category " + category + " not found!"));
    }

    @Override
    public void updateExample(Example example) throws ExampleNotFoundException, ExampleAlreadyExistException {

        Optional<Example> optionalExistingExample = exampleRepository.findById(example.getId());
        Boolean existByExampleTitle = exampleRepository.existByExampleTitle(example.getExampleTitle());

        if (optionalExistingExample.isPresent()) {
            Example existingExample = optionalExistingExample.get();
            existingExample.setId(example.getId());

            if (!existingExample.getExampleTitle().equals(example.getExampleTitle()) && existByExampleTitle) {
                throw new ExampleAlreadyExistException("Example " + example.getExampleTitle() + " already exist in database.");
            } else {
                existingExample.setExampleTitle(example.getExampleTitle());
            }

            existingExample.setCategory(example.getCategory());
            existingExample.setRightAnswer(example.getRightAnswer());

            exampleRepository.save(existingExample);
        } else {
            throw new ExampleNotFoundException("Example not found!");
        }
    }

    @Override
    public void deleteExampleById(Long id) {

        exampleRepository.deleteById(id);

    }

    @Override
    public Boolean getResult(Example example) {

        if (exampleRepository.findById(example.getId()).isPresent()) {
            Example exampleToCompare = exampleRepository.findById(example.getId()).get();
            example.setIsCorrect(example.getAnswer().equals(exampleToCompare.getRightAnswer()));

            log.info("User answer {} for example {} was: {}", example.getAnswer(), exampleToCompare.getExampleTitle(), example.getIsCorrect());

        }
        return example.getIsCorrect();
    }

}

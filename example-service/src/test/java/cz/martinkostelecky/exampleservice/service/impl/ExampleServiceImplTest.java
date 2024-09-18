package cz.martinkostelecky.exampleservice.service.impl;

import cz.martinkostelecky.exampleservice.entity.Example;
import cz.martinkostelecky.exampleservice.exception.ExampleAlreadyExistException;
import cz.martinkostelecky.exampleservice.exception.ExampleNotFoundException;
import cz.martinkostelecky.exampleservice.repository.ExampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExampleServiceImplTest {

    @Mock
    private ExampleRepository exampleRepository;

    private ExampleServiceImpl exampleServiceImplTest;

    @BeforeEach
    void setUp() {
        exampleServiceImplTest = new ExampleServiceImpl(exampleRepository);
    }

    @Test
    void canGetAllExamples() {
        exampleServiceImplTest.getAllExamples();

        verify(exampleRepository).findAll();
    }

    @Test
    void canSaveExample() throws ExampleAlreadyExistException {

        exampleServiceImplTest.saveExample(getTestExample());

        ArgumentCaptor<Example> exampleCaptor = ArgumentCaptor.forClass(Example.class);
        verify(exampleRepository).save(exampleCaptor.capture());

        Example capturedExample = exampleCaptor.getValue();

        assertEquals(getTestExample(), capturedExample);
    }

    @Test
    void willThrowExampleAlreadyExistException() {

        when(exampleRepository.existByExampleTitle("1+1")).thenReturn(true);

        ExampleAlreadyExistException exception = assertThrows(ExampleAlreadyExistException.class, () -> exampleServiceImplTest.saveExample(getTestExample()));

        assertEquals("Example " + getTestExample().getExampleTitle() + " already exist in database.", exception.getMessage());
    }

    @Test
    void canGetExampleById() throws ExampleNotFoundException {

        Long id = 1L;

        when(exampleRepository.findById(id)).thenReturn(Optional.of(getTestExample()));

        Example actualExample = exampleServiceImplTest.getExampleById(id);

        assertNotNull(actualExample);
        assertEquals(getTestExample(), actualExample);
    }

    @Test
    void willThrowExampleNotFoundException() {

        when(exampleRepository.findById(getTestExample().getId())).thenReturn(Optional.empty());

        //usage of AssertJ
        assertThatThrownBy(() -> exampleServiceImplTest.updateExample(getTestExample()))
                .isInstanceOf(ExampleNotFoundException.class)
                .hasMessageContaining("Example not found!");

        verify(exampleRepository, never()).save(getTestExample());
    }

    @Test
    void canGetExampleByCategory() throws ExampleNotFoundException {

        String category = "Sčítaní";

        when(exampleRepository.findByCategory(category)).thenReturn(List.of(getTestExample()));

        List<Example> exampleList = exampleServiceImplTest.getExamplesByCategory(category);

        assertEquals(List.of(getTestExample()).get(0), exampleList.get(0));
    }

    @Test
    void willThrowExampleNotFoundException_ByCategory() {

        when(exampleRepository.findByCategory(getTestExample().getCategory())).thenReturn(null);

        //usage of JUnit
        ExampleNotFoundException thrownException = assertThrows(
                ExampleNotFoundException.class,
                () -> exampleServiceImplTest.getExamplesByCategory(getTestExample().getCategory())
        );

        assertEquals("Examples of category " + getTestExample().getCategory() + " not found!", thrownException.getMessage());

    }

    @Test
    void shouldUpdateExample() throws ExampleNotFoundException, ExampleAlreadyExistException {

        Example toUpdateExample = new Example();

        toUpdateExample.setId(1L);
        toUpdateExample.setExampleTitle("2+2");
        toUpdateExample.setCategory("Sčítání");
        toUpdateExample.setRightAnswer("4");

        when(exampleRepository.findById(1L)).thenReturn(Optional.of(getTestExample()));

        when(exampleRepository.save(argThat(example ->
                "2+2".equals(example.getExampleTitle()) &&
                        "4".equals(example.getRightAnswer())
        ))).thenAnswer(invocation -> invocation.getArgument(0));

        exampleServiceImplTest.updateExample(toUpdateExample);

        // Verify that the save method was called with the correct arguments
        verify(exampleRepository, times(1)).save(argThat(example ->
                "2+2".equals(example.getExampleTitle()) &&
                        "4".equals(example.getRightAnswer()) &&
                        "Sčítání".equals(example.getCategory())
        ));

        Example updatedExample = exampleRepository.findById(1L).get();
        assertEquals("2+2", updatedExample.getExampleTitle());
        assertEquals("4", updatedExample.getRightAnswer());
        assertEquals("Sčítání", updatedExample.getCategory());
    }

    @Test
    void shouldDeleteExampleById() {

        doNothing().when(exampleRepository).deleteById(getTestExample().getId());

        assertThatCode(() -> exampleServiceImplTest.deleteExampleById(getTestExample().getId()))
                .doesNotThrowAnyException();

        verify(exampleRepository, times(1)).deleteById(eq(getTestExample().getId()));
    }

    @Test
    void canGetResult() {
        Example example = new Example();
        example.setId(1L);
        example.setAnswer("2");

        Example exampleToCompare = getTestExample();

        when(exampleRepository.findById(example.getId())).thenReturn(Optional.of(exampleToCompare));

        Boolean result = exampleServiceImplTest.getResult(example);

        assertTrue(result);
        assertTrue(example.getIsCorrect());

        verify(exampleRepository, times(2)).findById(example.getId());
    }

    private static Example getTestExample() {
        Example testExample = new Example();
        testExample.setId(1L);
        testExample.setExampleTitle("1+1");
        testExample.setCategory("Sčítání");
        testExample.setRightAnswer("2");

        return testExample;
    }
}
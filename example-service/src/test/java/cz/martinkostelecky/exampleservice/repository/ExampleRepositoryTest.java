package cz.martinkostelecky.exampleservice.repository;

import cz.martinkostelecky.exampleservice.entity.Example;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ExampleRepositoryTest {

    @Autowired
    private ExampleRepository exampleRepositoryTest;

    @AfterEach
    void tearDown() {
        exampleRepositoryTest.deleteAll();
    }

    @Test
    void itShouldCheckIfExampleExistByExampleTitle() {
        String exampleTitle = "1+1";

        Example testExample = new Example();
        testExample.setId(1L);
        testExample.setExampleTitle("1+1");
        testExample.setCategory("Sčítání");
        testExample.setRightAnswer("2");

        exampleRepositoryTest.save(testExample);

        Boolean expected = exampleRepositoryTest.existByExampleTitle(exampleTitle);

        assertTrue(expected);
    }

    @Test
    void itShouldCheckIfExampleDoesntExistByExampleTitle() {
        String exampleTitle = "1+1";

        Boolean expected = exampleRepositoryTest.existByExampleTitle(exampleTitle);

        assertFalse(expected);
    }

}
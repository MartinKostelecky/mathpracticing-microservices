package cz.martinkostelecky.practicingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Example {

    private Long id;

    private String exampleTitle;

    private String answer;

    private String category;

    private String rightAnswer;

    private Boolean isCorrect;
}

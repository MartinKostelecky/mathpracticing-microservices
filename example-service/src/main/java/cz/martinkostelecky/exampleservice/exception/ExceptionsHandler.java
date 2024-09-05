package cz.martinkostelecky.exampleservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = ExampleAlreadyExistException.class)
    public String handleBadRequestException(ExampleAlreadyExistException e, Model model) {

        model.addAttribute("status", HttpStatus.BAD_REQUEST);
        model.addAttribute("message", e.getMessage());

        return "error";
    }

    @ExceptionHandler(value = ExampleNotFoundException.class)
    public String handleInsuredPersonNotFoundException(ExampleNotFoundException e, Model model) {

        model.addAttribute("status", HttpStatus.NOT_FOUND);
        model.addAttribute("message", e.getMessage());

        return "error";
    }

}

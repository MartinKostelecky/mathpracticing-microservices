package cz.martinkostelecky.exampleservice.controller;

import cz.martinkostelecky.exampleservice.entity.Example;
import cz.martinkostelecky.exampleservice.exception.ExampleAlreadyExistException;
import cz.martinkostelecky.exampleservice.exception.ExampleNotFoundException;
import cz.martinkostelecky.exampleservice.service.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@RequiredArgsConstructor
//@RequestMapping(value = "math_practicing/admin")
public class ExampleController {

    private final ExampleService exampleService;

    @RequestMapping(value = "/examples", method = RequestMethod.GET)
    public String getAllExamples(Model model) {
        model.addAttribute("examples", exampleService.getAllExamples());
        return "examples";
    }


    @RequestMapping(value = "/add", method = GET)
    public String renderAddExamplesForm(Model model) {

        Example example = new Example();
        model.addAttribute("example", example);

        return "add_example";
    }

    @RequestMapping(value = "/examples", method = RequestMethod.POST)
    public String addExample(@ModelAttribute("example") Example example) throws ExampleAlreadyExistException {

        exampleService.saveExample(example);
        log.info("Added example: {}", example);
        return "redirect:/examples";
    }

    @RequestMapping(value = "/examples/edit/{id}", method = GET)
    public String renderEditExample(@PathVariable Long id, Model model) throws ExampleNotFoundException {
        model.addAttribute("example", exampleService.getExampleById(id));
        return "edit_example";
    }


    @RequestMapping(value = "/examples/{id}", method = POST)
    public String updateExample(@PathVariable Long id, @ModelAttribute("example") Example example) throws ExampleNotFoundException, ExampleAlreadyExistException {

        example.setId(id);
        exampleService.updateExample(example);

        return "redirect:/examples";
    }

    @RequestMapping(value = "/examples/{id}", method = GET)
    public String deleteExample(@PathVariable Long id) {

        exampleService.deleteExampleById(id);

        return "redirect:/examples";
    }

}


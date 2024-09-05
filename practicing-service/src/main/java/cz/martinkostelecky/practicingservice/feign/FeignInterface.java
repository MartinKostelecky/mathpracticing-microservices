package cz.martinkostelecky.practicingservice.feign;

import cz.martinkostelecky.practicingservice.model.Example;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("EXAMPLE-SERVICE")
public interface FeignInterface {

    //TODO produces = "application/json" ?
    @RequestMapping(value = "/get", produces = "application/json", method = RequestMethod.GET)
    Example getExampleById(@RequestParam Long id);

    //TODO produces = "application/json" ?
    @RequestMapping(value = "/getExample", produces = "application/json", method = RequestMethod.GET)
    List<Example> getExamplesByCategory(@RequestParam String category);

    @RequestMapping(value = "/getResult", produces = "application/json", method = RequestMethod.POST)
    Boolean getResult(@RequestBody Example example);

}

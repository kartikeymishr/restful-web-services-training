package com.kartikey.rest.webservices.restfulwebservices.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping(path = "/helloworld")
    public String getHelloWorld() {
        return "Hello World";
    }

    @GetMapping(path = "/helloworld-bean")
    public HelloWorldBean getHelloWorldBean() {
        return new HelloWorldBean("Hello World");
    }

    @GetMapping(path = "/helloworld/path-variable/{name}")
    public HelloWorldBean getHelloWorldPathVariable(@PathVariable String name) {
        return new HelloWorldBean("Hello World, " + name);
    }

    @GetMapping(path = "/helloworld-internationalized")
    public String getHelloWorldInternationalized() {
        return messageSource.getMessage("good.morning.message", null, "Default Message", LocaleContextHolder.getLocale());
    }
}

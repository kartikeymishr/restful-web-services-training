package com.kartikey.rest.webservices.restfulwebservices.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonVersioningController {

    // Utilizing URL for versioning
    // Used by Twitter
    // Good for caching; url pollution;
    @GetMapping(value = "v1/person")
    public PersonV1 getPersonV1() {
        return new PersonV1("Kartikey Mishr");
    }

    @GetMapping(value = "v2/person")
    public PersonV2 getPersonV2() {
        return new PersonV2(new Name("Kartikey", "Mishr"));
    }

    // Using query params for versioning
    // Used by Amazon
    // Good for caching; url pollution;
    @GetMapping(value = "/person/params", params = "version=1")
    public PersonV1 getParamV1() {
        return new PersonV1("Kartikey Mishr");
    }

    @GetMapping(value = "/person/params", params = "version=2")
    public PersonV2 getParamV2() {
        return new PersonV2(new Name("Kartikey", "Mishr"));
    }

    // Using headers for versioning
    // Used by Microsoft
    // Misuse of HTTP Headers
    @GetMapping(value = "/person/header", headers = "X-API-VERSION=1")
    public PersonV1 getHeaderV1() {
        return new PersonV1("Kartikey Mishr");
    }

    @GetMapping(value = "/person/header", headers = "X-API-VERSION=2")
    public PersonV2 getHeaderV2() {
        return new PersonV2(new Name("Kartikey", "Mishr"));
    }

    // Another method is Accept header versioning utilizing produces attribute for Mapping annotation
    // Also known as Content Negotiation
    // Used by GitHub
}

package com.kartikey.rest.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDaoService service;

    @GetMapping(path = "/users/all")
    public List<User> findAllUsers() {
        return service.findAll();
    }

    @PostMapping(path = "users/add")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        // Sends 201 Created along with URL as Location Header
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "users/{id}")
    public EntityModel<User> findUserById(@PathVariable Integer id) {
        User user = service.findById(id);
        if (user == null) {
            throw new UserNotFoundException("id :: " + id);
        }

        // Adding HATEOAS Links to Response Object
        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).findAllUsers());
        entityModel.add(linkToUsers.withRel("all-users"));

        return entityModel;
    }

    @DeleteMapping(path = "users/remove/{id}")
    public void deleteUser(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

package com.kartikey.rest.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJPAController {

    @Autowired
    private UserRepository repository;

    @GetMapping(path = "/jpa/users/all")
    public List<UserEntity> findAllUsers() {
        return repository.findAll();
    }

    @PostMapping(path = "/jpa/users/add")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserEntity user) {
        UserEntity savedUser = repository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        // Sends 201 Created along with URL as Location Header
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/jpa/users/{id}")
    public EntityModel<UserEntity> findUserById(@PathVariable Integer id) {
        Optional<UserEntity> userOptional = repository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id :: " + id);
        }

        // Adding HATEOAS Links to Response Object
        EntityModel<UserEntity> entityModel = EntityModel.of(userOptional.get());
        WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).findAllUsers());
        entityModel.add(linkToUsers.withRel("all-users"));

        return entityModel;
    }

    @DeleteMapping(path = "/jpa/users/remove/{id}")
    public void deleteUser(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

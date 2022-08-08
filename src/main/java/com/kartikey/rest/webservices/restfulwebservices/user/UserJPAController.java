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
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping(path = "/jpa/users/all")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/jpa/users/add")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        // Sends 201 Created along with URL as Location Header
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/jpa/users/{id}")
    public EntityModel<User> findUserById(@PathVariable Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id :: " + id);
        }

        // Adding HATEOAS Links to Response Object
        EntityModel<User> entityModel = EntityModel.of(userOptional.get());
        WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).findAllUsers());
        entityModel.add(linkToUsers.withRel("all-users"));

        return entityModel;
    }

    @DeleteMapping(path = "/jpa/users/remove/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    @GetMapping(value = "/jpa/users/{id}/posts")
    public ResponseEntity<List<Post>> getAllPostsByUser(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("id :: " + id);
        }

        return ResponseEntity.ok(user.get().getPosts());
    }

    @PostMapping(path = "/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPost(@RequestBody Post post, @PathVariable Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id :: " + id);
        }

        User user = userOptional.get();
        post.setUser(user);
        postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}

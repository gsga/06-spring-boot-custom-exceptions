package tup.msusers.controllers;

import java.net.URI;
import java.util.List;
// import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tup.msusers.exceptions.ResourceNotFoundException;
import tup.msusers.models.User;
import tup.msusers.repositories.UserRepository;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    @Autowired
    UserRepository userRepo;

    @GetMapping(value = "/users")
    List<User> getAll() {
        return userRepo.findAll();
    }

    @GetMapping(value = "/users/{id}")
    ResponseEntity<User> getById(@PathVariable("id") @Min(1) int id) {

        User usr = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID :" + id + " Not Found!"));

        return ResponseEntity.ok().body(usr);
    }

    @GetMapping(value = "/user")
    ResponseEntity<User> getByUsername(@RequestParam(required = true) String username) {

        User usr = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(username + " NOT Found!"));

        return ResponseEntity.ok().body(usr);
    }

    @PostMapping(value = "/users")
    ResponseEntity<?> create(@Valid @RequestBody User user) {

        User addeduser = userRepo.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addeduser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/users/{id}")
    ResponseEntity<User> update(@PathVariable("id") @Min(1) int id, @Valid @RequestBody User user) {

        User puser = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID :" + id + " Not Found!"));

        user.setId(puser.getId());
        userRepo.save(user);
        return ResponseEntity.ok().body(user);

    }

    @DeleteMapping(value = "/users/{id}")
    ResponseEntity<String> delete(@PathVariable("id") @Min(1) int id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID :" + id + " Not Found!"));

        userRepo.deleteById(user.getId());
        return ResponseEntity.ok().body("User deleted with success!");
    }
}

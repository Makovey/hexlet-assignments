package exercise.controller;

import exercise.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import exercise.model.Person;

@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping(path = "/{id}")
    public Person show(@PathVariable long id) {
        return personRepository.findById(id).get();
    }

    // BEGIN

    @GetMapping
    ResponseEntity<List<Person>> fetchAllPeople() {
        return ResponseEntity
                .ok(personRepository.findAll());
    }

    @PostMapping
    ResponseEntity<Person> createNewPerson(
            @RequestBody Person person
    ) {
        personRepository.save(person);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(person);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAllPeople(
            @PathVariable long id
    ) {
        personRepository.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
    // END
}

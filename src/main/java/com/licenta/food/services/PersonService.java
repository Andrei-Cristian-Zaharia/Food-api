package com.licenta.food.services;

import com.licenta.food.enums.ObjectType;
import com.licenta.food.exceptionHandlers.NotFoundException;
import com.licenta.food.models.Person;
import com.licenta.food.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person getPersonByName(String name) {
        Optional<Person> person = personRepository.findByUsername(name);

        return person.orElseThrow(() -> new NotFoundException(ObjectType.PERSON, name));
    }
}

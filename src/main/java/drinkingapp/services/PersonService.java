package drinkingapp.services;

import drinkingapp.commands.CreatePersonCommand;
import drinkingapp.commands.UpdatePersonCommand;
import drinkingapp.dtos.PersonDto;
import drinkingapp.exceptions.PersonNotFoundException;
import drinkingapp.model.Person;
import drinkingapp.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final ModelMapper mapper;

    public List<PersonDto> listPeople() {
        Type targetListType = new TypeToken<List<PersonDto>>() {
        }.getType();
        return mapper.map(personRepository.findAll(), targetListType);
    }

    public PersonDto findById(Long id) {
        return mapper.map(findPersonById(id), PersonDto.class);
    }

    public Person findPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    public PersonDto createPerson(CreatePersonCommand command) {
        Person person = personRepository.save(new Person(command.getName(), command.getSex(), command.getWeight()));
        return mapper.map(person, PersonDto.class);
    }

    @Transactional
    public PersonDto updatePerson(Long id, UpdatePersonCommand command) {
        Person person = findPersonById(id);
        person.setName(command.getName());
        person.setSex(command.getSex());
        person.setWeight(command.getWeight());
        return mapper.map(person, PersonDto.class);
    }

    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }
}

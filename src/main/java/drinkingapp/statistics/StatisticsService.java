package drinkingapp.statistics;

import drinkingapp.person.Person;
import drinkingapp.person.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final PersonService personService;

    public PersonStatistics getStatistics(Long id) {
        Person person = personService.findPersonById(id);
        return new PersonStatistics(person);
    }
}

package drinkingapp.services;

import drinkingapp.commands.CreateDrinkCommand;
import drinkingapp.commands.UpdateDrinkCommand;
import drinkingapp.dtos.DrinkDto;
import drinkingapp.model.Drink;
import drinkingapp.model.Person;
import drinkingapp.repositories.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;
    private final PersonService personService;
    private final ModelMapper mapper;

    public List<DrinkDto> listDrinks(Long id) {
        Type targetListType = new TypeToken<List<DrinkDto>>() {
        }.getType();
        return mapper.map(drinkRepository.findAllByPerson_Id(id), targetListType);
    }

    public DrinkDto findById(Long id, Long drinkId) {
        Drink drink = drinkRepository.findByIdAndPerson_id(drinkId, id);
        return mapper.map(drink, DrinkDto.class);
    }

    public DrinkDto createDrink(Long id, CreateDrinkCommand command) {
        Person person = personService.findPersonById(id);
        Drink drink = drinkRepository.save(
                new Drink(command.getName(), command.getVolume(), command.getAlcoholPercent(),
                        command.getElapsedTime(), person));
        return mapper.map(drink, DrinkDto.class);
    }

    @Transactional
    public DrinkDto updateDrink(Long id, Long drinkId, UpdateDrinkCommand command) {
        Drink drink = drinkRepository.findByIdAndPerson_id(drinkId, id);
        drink.setName(command.getName());
        drink.setVolume(command.getVolume());
        drink.setAlcoholPercent(command.getAlcoholPercent());
        drink.setElapsedTime(command.getElapsedTime());
        drink.calculateGramAmountOfAlcohol();
        return mapper.map(drink, DrinkDto.class);
    }

    public void deleteById(Long id, Long drinkId) {
        Drink drink = drinkRepository.findByIdAndPerson_id(drinkId, id);
        drinkRepository.delete(drink);
    }
}

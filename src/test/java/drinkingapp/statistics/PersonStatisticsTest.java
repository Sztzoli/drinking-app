package drinkingapp.statistics;

import drinkingapp.drink.Drink;
import drinkingapp.person.Person;
import drinkingapp.person.Sex;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonStatisticsTest {

    @Test
    void getStatisticsAfterOneDrink() {
        Person person = new Person("John Doe", Sex.MALE, 80);
        List<Drink> drinks = new ArrayList<>();
        Drink drink = new Drink("beer", BigDecimal.valueOf(500), BigDecimal.valueOf(5), 0L, person);
        drink.calculateElapsedTime();
        drink.calculateGramAmountOfAlcohol();
        drinks.add(drink);
        person.setDrinks(drinks);

        PersonStatistics personStatistics = new PersonStatistics(person);
        assertEquals(new BigDecimal("20.0"), personStatistics.getAllAlcohol());
        assertEquals(new BigDecimal("0"), personStatistics.getElapsedTime());
        assertEquals(new BigDecimal("0.368"), personStatistics.getActualAlcoholContent());
        assertEquals(new BigDecimal("20.0"), personStatistics.getActualAlcoholVolume());
        assertEquals(new BigDecimal("147"), personStatistics.getSoberTime());
    }

    @Test
    void getStatisticsAfterSoberTime() {
        Person person = new Person("John Doe", Sex.MALE, 80);
        List<Drink> drinks = new ArrayList<>();
        Drink drink = new Drink("beer", BigDecimal.valueOf(500), BigDecimal.valueOf(5), 148L, person);
        drink.calculateElapsedTime();
        drink.calculateGramAmountOfAlcohol();
        drinks.add(drink);
        person.setDrinks(drinks);

        PersonStatistics personStatistics = new PersonStatistics(person);
        assertEquals(new BigDecimal("20.0"), personStatistics.getAllAlcohol());
        assertEquals(new BigDecimal("148"), personStatistics.getElapsedTime());
        assertEquals(new BigDecimal("0"), personStatistics.getActualAlcoholContent());
        assertEquals(new BigDecimal("0.0"), personStatistics.getActualAlcoholVolume());
        assertEquals(new BigDecimal("0"), personStatistics.getSoberTime());
    }

    @Test
    void getStatisticsAfterOneDrinkWomen() {
        Person person = new Person("Jane Doe", Sex.FEMALE, 80);
        List<Drink> drinks = new ArrayList<>();
        Drink drink = new Drink("beer", BigDecimal.valueOf(500), BigDecimal.valueOf(5), 0L, person);
        drink.calculateElapsedTime();
        drink.calculateGramAmountOfAlcohol();
        drinks.add(drink);
        person.setDrinks(drinks);

        PersonStatistics personStatistics = new PersonStatistics(person);
        assertEquals(new BigDecimal("20.0"), personStatistics.getAllAlcohol());
        assertEquals(new BigDecimal("0"), personStatistics.getElapsedTime());
        assertEquals(new BigDecimal("0.455"), personStatistics.getActualAlcoholContent());
        assertEquals(new BigDecimal("20.0"), personStatistics.getActualAlcoholVolume());
        assertEquals(new BigDecimal("161"), personStatistics.getSoberTime());
    }

    @Test
    void getStatisticsAfterSomeDrink() {
        Person person = new Person("John Doe", Sex.MALE, 80);
        List<Drink> drinks = new ArrayList<>();
        Drink drink = new Drink("beer", BigDecimal.valueOf(500), BigDecimal.valueOf(5), 0L, person);
        drink.calculateElapsedTime();
        drink.calculateGramAmountOfAlcohol();
        drinks.add(drink);
        Drink drink2 = new Drink("beer", BigDecimal.valueOf(500), BigDecimal.valueOf(5), 60L, person);
        drink2.calculateElapsedTime();
        drink2.calculateGramAmountOfAlcohol();
        drinks.add(drink2);
        person.setDrinks(drinks);

        PersonStatistics personStatistics = new PersonStatistics(person);
        assertEquals(new BigDecimal("40.0"), personStatistics.getAllAlcohol());
        assertEquals(new BigDecimal("60"), personStatistics.getElapsedTime());
        assertEquals(new BigDecimal("0.585"), personStatistics.getActualAlcoholContent());
        assertEquals(new BigDecimal("31.8"), personStatistics.getActualAlcoholVolume());
        assertEquals(new BigDecimal("234"), personStatistics.getSoberTime());
    }

}
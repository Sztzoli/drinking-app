package drinkingapp.controllers;

import drinkingapp.drink.CreateDrinkCommand;
import drinkingapp.drink.DrinkDto;
import drinkingapp.person.CreatePersonCommand;
import drinkingapp.person.PersonDto;
import drinkingapp.person.Sex;
import drinkingapp.statistics.PersonStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {
        "delete from drinks",
        "delete from people"})
public class StatisticsControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    static String URL_PEOPLE = "/api/people";
    static String URL_DRINKS = "/api/people/{id}/drinks";
    static String URL = "/api/people/{id}/statistics";
    static Map<String, String> params = new HashMap<>();

    @BeforeEach
    void setUp() {
        PersonDto person = template.postForObject(URL_PEOPLE,
                new CreatePersonCommand("John Doe", Sex.MALE, 80),
                PersonDto.class
        );
        params.put("id", person.getId().toString());
        template.postForObject(URL_DRINKS,
                new CreateDrinkCommand("beer", new BigDecimal(500), new BigDecimal(5), 0L),
                DrinkDto.class,
                params);
    }

    @Test
    void getStatistics() {
        PersonStatistics personStatistics = template.getForObject(URL, PersonStatistics.class, params);
        assertEquals(new BigDecimal("20.0"), personStatistics.getAllAlcohol());
        assertEquals(new BigDecimal("0"), personStatistics.getElapsedTime());
        assertEquals(new BigDecimal("0.368"), personStatistics.getActualAlcoholContent());
        assertEquals(new BigDecimal("20.0"), personStatistics.getActualAlcoholVolume());
        assertEquals(new BigDecimal("147"), personStatistics.getSoberTime());

    }
}

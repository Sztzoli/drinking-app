package drinkingapp.controllers;

import drinkingapp.commands.CreateDrinkCommand;
import drinkingapp.commands.CreatePersonCommand;
import drinkingapp.commands.UpdateDrinkCommand;
import drinkingapp.dtos.DrinkDto;
import drinkingapp.dtos.PersonDto;
import drinkingapp.model.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {
        "delete from drinks",
})
class DrinkControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    static String URL_PEOPLE = "/api/people";
    static String URL = "/api/people/{id}/drinks";
    static String URL_WITH_DRINK_ID = "/api/people/{id}/drinks/{drinkId}";
    static Map<String, String> params = new HashMap<>();

    PersonDto person;
    DrinkDto drink;

    @BeforeEach
    void setUp() {
        person = template.postForObject(URL_PEOPLE,
                new CreatePersonCommand("John Doe", Sex.MALE, 80),
                PersonDto.class
        );
        params.put("id", person.getId().toString());
        drink = template.postForObject(URL,
                new CreateDrinkCommand("beer", new BigDecimal(500), new BigDecimal(5), 0L),
                DrinkDto.class,
                params);
        params.put("drinkId", drink.getId().toString());
    }

    @Test
    void save() {
        assertEquals("beer", drink.getName());
        assertEquals(0L, drink.getElapsedTime());
        assertEquals(new BigDecimal("20.000"), drink.getAmountOfAlcohol());
    }

    @Test
    void findById() {
        DrinkDto result = template.getForObject(URL_WITH_DRINK_ID, DrinkDto.class, params);

        assertEquals("beer", result.getName());
        assertEquals(0L, result.getElapsedTime());
    }

    @Test
    void listAll() {
        template.postForObject(URL,
                new CreateDrinkCommand("wine", new BigDecimal(200), new BigDecimal(10), 0L),
                DrinkDto.class,
                params);

        List<DrinkDto> result = template.exchange(URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DrinkDto>>() {
                }, params).getBody();

        assertThat(result).
                hasSize(2)
                .extracting(DrinkDto::getName)
                .containsExactly("beer", "wine");
    }

    @Test
    void update() {
        DrinkDto result = template.exchange(URL_WITH_DRINK_ID,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateDrinkCommand("wine", new BigDecimal(500), new BigDecimal(10), 60L)),
                DrinkDto.class,
                params).getBody();

        assertEquals("wine", result.getName());
        assertEquals(new BigDecimal("40.000"), result.getAmountOfAlcohol());
        assertEquals(60L, result.getElapsedTime());
    }

    @Test
    void deleteById() {
        template.delete(URL_WITH_DRINK_ID, params);

        List<DrinkDto> result = template.exchange(URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DrinkDto>>() {
                }, params).getBody();

        assertThat(result).
                hasSize(0);
    }

    @Test
    void invalidPostNameEmpty() {
        Problem problem = template.postForObject(URL,
                new CreateDrinkCommand("", new BigDecimal(200), new BigDecimal(10), 0L),
                Problem.class,
                params);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidPostNameLength() {
        Problem problem = template.postForObject(URL,
                new CreateDrinkCommand("a".repeat(256), new BigDecimal(200), new BigDecimal(10), 0L),
                Problem.class,
                params);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidUpdateNameEmpty() {
        Problem problem = template.exchange(URL_WITH_DRINK_ID,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateDrinkCommand("", new BigDecimal(500), new BigDecimal(10), 60L)),
                Problem.class,
                params).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidUpdateNameLength() {
        Problem problem = template.exchange(URL_WITH_DRINK_ID,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateDrinkCommand("a".repeat(256), new BigDecimal(500), new BigDecimal(10), 60L)),
                Problem.class,
                params).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidPostVolumeNotPositive() {
        Problem problem = template.postForObject(URL,
                new CreateDrinkCommand("beer", new BigDecimal(-1), new BigDecimal(10), 0L),
                Problem.class,
                params);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidUpdateVolumeNotPositive() {
        Problem problem = template.exchange(URL_WITH_DRINK_ID,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateDrinkCommand("beer", new BigDecimal(-1), new BigDecimal(10), 60L)),
                Problem.class,
                params).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidPostAlcoholPercentNotPositive() {
        Problem problem = template.postForObject(URL,
                new CreateDrinkCommand("beer", new BigDecimal(500), new BigDecimal(-1), 0L),
                Problem.class,
                params);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidUpdateAlcoholPercentNotPositive() {
        Problem problem = template.exchange(URL_WITH_DRINK_ID,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateDrinkCommand("beer", new BigDecimal(500), new BigDecimal(-1), 60L)),
                Problem.class,
                params).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidPostElapsedTimeNotPositive() {
        Problem problem = template.postForObject(URL,
                new CreateDrinkCommand("beer", new BigDecimal(500), new BigDecimal(5), -1L),
                Problem.class,
                params);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidUpdateElapsedTimeNotPositive() {
        Problem problem = template.exchange(URL_WITH_DRINK_ID,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateDrinkCommand("beer", new BigDecimal(500), new BigDecimal(5), -1L)),
                Problem.class,
                params).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

}
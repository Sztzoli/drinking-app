package drinkingapp.controllers;

import drinkingapp.commands.CreatePersonCommand;
import drinkingapp.commands.UpdatePersonCommand;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from people"})
class PersonControllerRestTemplateIT {

    @Autowired
    TestRestTemplate template;

    static String URL = "/api/people";
    static Map<String, String> params = new HashMap<>();

    PersonDto person;

    @BeforeEach
    void setUp() {
        person = template.postForObject(URL,
                new CreatePersonCommand("John Doe", Sex.MALE, 80),
                PersonDto.class
        );
        params.put("id", person.getId().toString());
    }

    @Test
    void save() {
        assertEquals("John Doe", person.getName());
    }

    @Test
    void findById() {
        PersonDto result = template.getForObject(URL + "/{id}", PersonDto.class, params);

        assertEquals(Sex.MALE, result.getSex());
        assertEquals(80, result.getWeight());
    }

    @Test
    void listAll() {
        template.postForObject(URL,
                new CreatePersonCommand("Jane Doe", Sex.FEMALE, 75),
                PersonDto.class
        );

        List<PersonDto> result = template.exchange(URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PersonDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting(PersonDto::getName)
                .containsExactly("John Doe", "Jane Doe");
    }

    @Test
    void Update() {
        PersonDto result = template.exchange(URL + "/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdatePersonCommand("John Doe update", Sex.FEMALE, 65)),
                PersonDto.class,
                params).getBody();

        assertEquals("John Doe update", result.getName());
        assertEquals(Sex.FEMALE, result.getSex());
        assertEquals(65, result.getWeight());
    }

    @Test
    void DeleteById() {
        template.delete(URL + "/{id}", params);

        List<PersonDto> result = template.exchange(URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PersonDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(0);
    }

    @Test
    void invalidNamePostEmpty() {
        Problem problem = template
                .postForObject(URL, new CreatePersonCommand("", Sex.MALE, 80), Problem.class);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidNamePostLength() {
        Problem problem = template
                .postForObject(URL, new CreatePersonCommand("a".repeat(256), Sex.MALE, 80), Problem.class);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidWeightPostNotPositive() {
        Problem problem = template
                .postForObject(URL, new CreatePersonCommand("John Doe", Sex.MALE, -1), Problem.class);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidSexPostNull() {
        Problem problem = template
                .postForObject(URL, new CreatePersonCommand("John Doe", null, 80), Problem.class);

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidNameUpdateEmpty() {
        Problem problem = template.exchange(URL + "/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdatePersonCommand("", Sex.FEMALE, 65)),
                Problem.class,
                params).getBody();


        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidNameUpdateLength() {
        Problem problem = template.exchange(URL + "/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdatePersonCommand("a".repeat(256), Sex.FEMALE, 65)),
                Problem.class,
                params).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidWeightUpdateNotPositive() {
        Problem problem = template.exchange(URL + "/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdatePersonCommand("John Doe", Sex.FEMALE, -1)),
                Problem.class,
                params).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void invalidSexUpdateNull() {
        Problem problem = template.exchange(URL + "/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdatePersonCommand("John Doe", null, 80)),
                Problem.class,
                params).getBody();

        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("Constraint Violation", problem.getTitle());
    }

    @Test
    void personNotFound() {
        params.put("id", "-1");
        Problem problem = template.getForObject(URL + "/{id}", Problem.class, params);

        assertEquals(Status.NOT_FOUND, problem.getStatus());
        assertEquals("Person not found by id: -1", problem.getDetail());
        assertEquals("Not found", problem.getTitle());
    }

}
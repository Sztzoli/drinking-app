package drinkingapp.person;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PersonNotFoundException extends AbstractThrowableProblem {

    public PersonNotFoundException(Long id) {
        super(
                URI.create("person/not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Person not found by id: %d", id)
        );
    }
}

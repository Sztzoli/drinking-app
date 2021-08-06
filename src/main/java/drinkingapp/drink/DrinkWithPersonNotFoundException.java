package drinkingapp.drink;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class DrinkWithPersonNotFoundException extends AbstractThrowableProblem {

    public DrinkWithPersonNotFoundException(Long id, Long personId) {
        super(
                URI.create("drink/not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Drink not found by id: %d for person id: %d", id, personId)
        );
    }
}

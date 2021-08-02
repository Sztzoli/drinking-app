package drinkingapp.drink;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class DrinkNotFoundException extends AbstractThrowableProblem {

    public DrinkNotFoundException(Long id) {
        super(
                URI.create("drink/not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Drink not found by id: %d", id)
        );
    }
}

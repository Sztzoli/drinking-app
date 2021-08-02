package drinkingapp.person;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public enum Sex {
    MALE(new BigDecimal("0.68"),new BigDecimal("0.15")),
    FEMALE(new BigDecimal("0.55"),new BigDecimal("0.17"));

    private final BigDecimal ratioOfBodyWater;
    private final BigDecimal ratioOfMetabolism;
}

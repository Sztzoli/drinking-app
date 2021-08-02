package drinkingapp.statistics;

import drinkingapp.drink.Drink;
import drinkingapp.person.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PersonStatistics {

    private BigDecimal allAlcohol;
    private BigDecimal elapsedTime;
    private BigDecimal actualAlcoholContent;
    private BigDecimal actualAlcoholVolume;
    private BigDecimal soberTime;


    public PersonStatistics(Person person) {
        this.allAlcohol = calculateAllAlcohol(person);
        this.elapsedTime = calculateElapsedTime(person);
        this.actualAlcoholContent = calculateWidmarkFormula(person);
        this.actualAlcoholVolume = calculateActualAlcoholVolume(person);
        this.soberTime = calculateSoberTime(person);
    }

    private BigDecimal calculateWidmarkFormula(Person person) {
        BigDecimal result = allAlcohol
                .divide(calculateWeightMultiplyBodyWater(person), 3, RoundingMode.HALF_UP)
                .subtract(person.getSex().getRatioOfMetabolism().multiply(elapsedTime).divide(new BigDecimal("60"), 3, RoundingMode.HALF_UP));
        return result.setScale(3, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) < 0
                ? BigDecimal.ZERO
                : result.setScale(3, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateWeightMultiplyBodyWater(Person person) {
        return BigDecimal.valueOf(person.getWeight()).multiply(person.getSex().getRatioOfBodyWater());
    }

    private BigDecimal calculateAllAlcohol(Person person) {
        BigDecimal result = person.getDrinks().stream().map(Drink::getAmountOfAlcohol)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);
        return result.setScale(1, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateElapsedTime(Person person) {
        long max = person.getDrinks().stream().collect(Collectors.summarizingLong(Drink::getElapsedTime)).getMax();
        return new BigDecimal(max);
    }

    private BigDecimal calculateActualAlcoholVolume(Person person) {
        BigDecimal result = actualAlcoholContent.multiply(calculateWeightMultiplyBodyWater(person));
        return result.setScale(1, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSoberTime(Person person) {
        BigDecimal result = actualAlcoholContent.divide(person.getSex().getRatioOfMetabolism(), 3, RoundingMode.HALF_UP).multiply(new BigDecimal("60"));
        return result.setScale(0, RoundingMode.HALF_UP);
    }
}

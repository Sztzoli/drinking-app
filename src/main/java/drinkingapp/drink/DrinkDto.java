package drinkingapp.drink;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrinkDto {

    private Long id;

    private String name;

    private BigDecimal volume;

    private BigDecimal alcoholPercent;

    private BigDecimal AmountOfAlcohol;

    private long elapsedTime;

}

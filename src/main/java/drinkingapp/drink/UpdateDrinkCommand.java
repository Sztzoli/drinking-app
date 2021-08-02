package drinkingapp.drink;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDrinkCommand {

    @NotBlank(message = "Drink's name cannot be null or blank")
    @Length(max = 255, message = "Drink's name maximum length is 255")
    @Schema(description = "Drink's name", example = "Beer")
    private String name;

    @Positive
    @Schema(description = "Drink's volume - ml", example = "500")
    private BigDecimal volume;

    @Positive
    @Schema(description = "Drink's alcohol percent", example = "500")
    private BigDecimal alcoholPercent;

    @PositiveOrZero
    @Schema(description = "elapsed time since the start of the drink - min", example = "0")
    private long elapsedTime;
}

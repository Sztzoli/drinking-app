package drinkingapp.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePersonCommand {

    @NotBlank(message = "Person's name cannot be null or blank")
    @Length(max = 255, message = "Person's name maximum length is 255")
    @Schema(description = "Person's name", example = "John Doe")
    private String name;


    @Schema(description = "Person's sex", example = "MALE")
    @NotNull
    private Sex sex;

    @NotNull(message = "Person's weight cannot be null")
    @Positive(message = "Person's weight cannot be o or negative" )
    @Schema(description = "Person's weight in kilogram", example = "80")
    private int weight;
}

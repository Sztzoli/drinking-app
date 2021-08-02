package drinkingapp.controllers;

import drinkingapp.commands.CreateDrinkCommand;
import drinkingapp.commands.UpdateDrinkCommand;
import drinkingapp.dtos.DrinkDto;
import drinkingapp.services.DrinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/people/{id}/drinks")
@RequiredArgsConstructor
@Tag(name = "operations on drinks")
public class DrinkController {

    private final DrinkService drinkService;

    @GetMapping
    @Operation(summary = "list all drink of person",
            description = "list all drink of person by person's id")
    public List<DrinkDto> listDrinks(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id
    ) {
        return drinkService.listDrinks(id);
    }

    @GetMapping("/{drinkId}")
    @Operation(summary = "receive a person's drink",
            description = "receive a person's drink by id")
    @ApiResponse(responseCode = "400", description = "person is invalid")
    @ApiResponse(responseCode = "404", description = "drink not found by id")
    public DrinkDto findById(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id,
            @Parameter(description = "id of drink", example = "1") @PathVariable Long drinkId
    ) {
        return drinkService.findById(id, drinkId);
    }

    @PostMapping()
    @Operation(summary = "create a drink")
    @ApiResponse(responseCode = "201", description = "drink has been created")
    @ApiResponse(responseCode = "400", description = "validation exception when created drink")
    @ApiResponse(responseCode = "404", description = "person not found by id")
    public DrinkDto createDrink(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id,
            @Valid @RequestBody CreateDrinkCommand command
    ) {
        return drinkService.createDrink(id, command);
    }

    @PutMapping("/{drinkId}")
    @Operation(summary = "update a drink",
            description = "update drink by id")
    @ApiResponse(responseCode = "400", description = "person is invalid")
    @ApiResponse(responseCode = "400", description = "validation exception when updated drink")
    @ApiResponse(responseCode = "404", description = "drink not found by id")
    public DrinkDto updateDrink(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id,
            @Parameter(description = "id of drink", example = "1") @PathVariable Long drinkId,
            @Valid @RequestBody UpdateDrinkCommand command
    ) {
        return drinkService.updateDrink(id, drinkId, command);
    }

    @DeleteMapping("/{drinkId}")
    @Operation(summary = "delete a drink",
            description = "delete drink by id")
    @ApiResponse(responseCode = "204", description = "no content")
    @ApiResponse(responseCode = "400", description = "person is invalid")
    @ApiResponse(responseCode = "404", description = "drink not found by id")
    public void deleteById(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id,
            @Parameter(description = "id of drink", example = "1") @PathVariable Long drinkId
    ) {
        drinkService.deleteById(id, drinkId);
    }
}

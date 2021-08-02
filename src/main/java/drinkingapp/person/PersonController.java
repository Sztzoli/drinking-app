package drinkingapp.person;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/people")
@RequiredArgsConstructor
@Tag(name = "operations on people")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @Operation(summary = "list all person")
    public List<PersonDto> listPeople() {
        return personService.listPeople();
    }

    @GetMapping("/{id}")
    @Operation(summary = "receive a person",
            description = "receive a person by id")
    @ApiResponse(responseCode = "404", description = "person not found by id")
    public PersonDto findById(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id
    ) {
        return personService.findById(id);
    }

    @PostMapping()
    @Operation(summary = "create a person")
    @ApiResponse(responseCode = "201", description = "person has been created")
    @ApiResponse(responseCode = "400", description = "validation exception when created person")
    public PersonDto createPerson(
            @Valid @RequestBody CreatePersonCommand command
    ) {
        return personService.createPerson(command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update a person",
            description = "update person by id")
    @ApiResponse(responseCode = "400", description = "validation exception when updated person")
    @ApiResponse(responseCode = "404", description = "person not found by id")
    public PersonDto updatePerson(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdatePersonCommand command
    ) {
        return personService.updatePerson(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a person",
            description = "delete person by id")
    @ApiResponse(responseCode = "204", description = "no content")
    @ApiResponse(responseCode = "404", description = "person not found by id")
    public void deleteById(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id
    ) {
        personService.deleteById(id);
    }
}

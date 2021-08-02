package drinkingapp.statistics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/people/{id}/statistics")
@RequiredArgsConstructor
@Tag(name = "statistics information for a person")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping()
    @Operation(summary = "receive a person's statistics",
            description = "receive a person's statistics by id")
    @ApiResponse(responseCode = "404", description = "person not found by id")
    public PersonStatistics getStatistics(
            @Parameter(description = "id of person", example = "1") @PathVariable Long id
    ) {
        return statisticsService.getStatistics(id);
    }
}

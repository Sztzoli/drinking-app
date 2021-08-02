package drinkingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@NoArgsConstructor
@Table(name = "drinks")
public class Drink {

    public static final BigDecimal DENSITY_OF_ALCOHOL = new BigDecimal("0.8");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal volume;

    private BigDecimal alcoholPercent;

    private BigDecimal amountOfAlcohol;

    private LocalDateTime time;

    @Transient
    private long elapsedTime;

    @ManyToOne
    private Person person;

    public Drink(String name, BigDecimal volume, BigDecimal alcoholPercent, long elapsedTime, Person person) {
        this.name = name;
        this.volume = volume;
        this.alcoholPercent = alcoholPercent;
        this.time = LocalDateTime.now().minusMinutes(elapsedTime);
        this.person = person;
    }

    @PrePersist
    public void calculateGramAmountOfAlcohol() {
        this.amountOfAlcohol = DENSITY_OF_ALCOHOL
                .multiply(volume)
                .multiply(alcoholPercent)
                .divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }


    @PostLoad
    @PostPersist
    public void calculateElapsedTime() {
        elapsedTime = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
    }
}

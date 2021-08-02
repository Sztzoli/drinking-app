package drinkingapp.person;

import drinkingapp.drink.Drink;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private int weight;

    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private List<Drink> drinks;


    public Person(String name, Sex sex, int weight) {
        this.name = name;
        this.sex = sex;
        this.weight = weight;
    }
}

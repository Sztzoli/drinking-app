package drinkingapp.drink;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DrinkRepository extends JpaRepository<Drink, Long> {

    List<Drink> findAllByPerson_Id(Long personId);

    Optional<Drink> findByIdAndPerson_id(Long id, Long personId);
}

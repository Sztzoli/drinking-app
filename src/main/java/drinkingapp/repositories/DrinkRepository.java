package drinkingapp.repositories;

import drinkingapp.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrinkRepository extends JpaRepository<Drink, Long> {

    List<Drink> findAllByPerson_Id(Long personId);

    Drink findByIdAndPerson_id(Long id, Long personId);
}

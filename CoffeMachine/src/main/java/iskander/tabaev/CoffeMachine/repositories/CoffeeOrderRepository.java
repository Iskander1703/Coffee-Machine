package iskander.tabaev.CoffeMachine.repositories;

import iskander.tabaev.CoffeMachine.models.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Integer> {
    public List<CoffeeOrder> findByDateLessThanAndDateGreaterThan(Date date, Date date2);
    public Optional<CoffeeOrder> findById(Integer id);
}

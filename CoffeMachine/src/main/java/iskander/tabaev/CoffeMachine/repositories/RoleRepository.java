package iskander.tabaev.CoffeMachine.repositories;

import iskander.tabaev.CoffeMachine.models.Role;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Optional<Role> findByRole(String role);
}

package bg.magna.websop.repository;

import bg.magna.websop.model.entity.User;
import bg.magna.websop.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> getUserByEmail(String email);
    List<User> findAllByUserRole(UserRole userRole);
}

package bg.magna.websop.repository;

import bg.magna.websop.model.entity.UserEntity;
import bg.magna.websop.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    Optional<UserEntity> getUserByEmail(String email);
    List<UserEntity> findAllByUserRole(UserRole userRole);
    Optional<UserEntity> findByEmail(String email);
}

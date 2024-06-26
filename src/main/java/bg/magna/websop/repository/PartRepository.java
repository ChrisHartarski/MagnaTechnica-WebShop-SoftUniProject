package bg.magna.websop.repository;

import bg.magna.websop.model.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, String> {
    boolean existsByPartCode(String partCode);
}

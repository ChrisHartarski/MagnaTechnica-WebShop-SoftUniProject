package bg.magna.websop.repository;

import bg.magna.websop.model.entity.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, String> {
    Page<Part> findByPartCodeContains(String partCode, Pageable pageable);

    boolean existsByPartCode(String partCode);

    Part getByPartCode(String partCode);

    void deleteByPartCode(String partCode);

    Optional<Part> findByPartCode(String partCode);
}

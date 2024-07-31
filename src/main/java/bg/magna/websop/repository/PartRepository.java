package bg.magna.websop.repository;

import bg.magna.websop.model.dto.part.ShortPartDataDTO;
import bg.magna.websop.model.entity.Part;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, String> {

    boolean existsByPartCode(String partCode);

    Part getByPartCode(String partCode);

    void deleteByPartCode(String partCode);

    Optional<Part> findByPartCode(String partCode);
}

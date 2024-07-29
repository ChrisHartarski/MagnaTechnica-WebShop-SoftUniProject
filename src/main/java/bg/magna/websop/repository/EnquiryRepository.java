package bg.magna.websop.repository;

import bg.magna.websop.model.entity.Enquiry;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    void deleteByCreatedOnBefore(LocalDateTime before);
}

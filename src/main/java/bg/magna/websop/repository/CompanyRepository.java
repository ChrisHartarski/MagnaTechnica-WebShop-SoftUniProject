package bg.magna.websop.repository;

import bg.magna.websop.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Optional<Company> findByName(String name);

    boolean existsByName(String name);

    boolean existsByVatNumber(String vatNumber);
}

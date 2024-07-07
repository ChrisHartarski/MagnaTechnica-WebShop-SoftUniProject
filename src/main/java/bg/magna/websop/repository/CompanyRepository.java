package bg.magna.websop.repository;

import bg.magna.websop.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Company getByName(String name);
    boolean existsByName(String name);
}

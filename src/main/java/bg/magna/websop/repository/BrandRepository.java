package bg.magna.websop.repository;

import bg.magna.websop.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);

    Optional<Brand> findByName(String brandName);
}

package bg.magna.websop.repository;

import bg.magna.websop.model.entity.PartOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartOrderRepository extends JpaRepository<PartOrder, Long> {
}

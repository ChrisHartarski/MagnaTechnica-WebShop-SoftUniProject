package bg.magna.websop.repository;

import bg.magna.websop.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByDispatchedOnNull();
    List<Order> findAllByDispatchedOnNotNullAndDeliveredOnNull();
    List<Order> findAllByDeliveredOnNotNull();
}

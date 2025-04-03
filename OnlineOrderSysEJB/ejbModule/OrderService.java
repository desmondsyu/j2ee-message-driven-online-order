import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Stateless
public class OrderService {
    @PersistenceContext(unitName = "OrderProcessingPU")
    private EntityManager em;

    @Transactional
    public void saveOrder(Order order) {
        em.persist(order);
    }

    @Transactional
    public Order getOrderById(Integer orderId) {
        return em.find(Order.class, orderId);
    }

    @Transactional
    public void updateOrder(Integer orderId) {
        Order order = em.find(Order.class, orderId);
        if (order != null) {
            order.setStatus("Processed");
            em.merge(order);
        }
    }
}

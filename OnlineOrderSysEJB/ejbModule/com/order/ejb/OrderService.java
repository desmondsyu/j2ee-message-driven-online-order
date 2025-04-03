package com.order.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class OrderService {

    @PersistenceContext(unitName = "OrderProcessingPU")
    private EntityManager em;

    public void saveOrder(OrderEntity orderEntity) {
        em.persist(orderEntity);
    }

    public OrderEntity getOrderById(Integer orderId) {
        return em.find(OrderEntity.class, orderId);
    }

    public boolean updateOrder(Integer orderId) {
        OrderEntity orderEntity = em.find(OrderEntity.class, orderId);
        if (orderEntity != null) {
            orderEntity.setStatus("Processed");
            em.merge(orderEntity);
            return true;
        }
        return false;
    }
}

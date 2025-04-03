package com.order.ejb;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Queue;


@Stateless
public class OrderMessageProducer {
    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/queue/OrderProcessingQueue")
    private Queue queue;

    public void sendOrderMessage(Integer orderId, String status) {
        try (JMSContext context = connectionFactory.createContext()) {
            JMSProducer producer = context.createProducer();
            String message = orderId + "," + status;
            producer.send(queue, message);
            System.out.println("Sent JMS message: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

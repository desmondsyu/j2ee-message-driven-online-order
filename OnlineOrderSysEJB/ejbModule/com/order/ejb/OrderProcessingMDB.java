package com.order.ejb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.*;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/queue/OrderProcessingQueue")
        }
)
public class OrderProcessingMDB implements MessageListener {

    @EJB
    private OrderService orderService;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String[] parts = textMessage.getText().split(",");
                Integer orderId = Integer.parseInt(parts[0]);
                String status = parts[1];

                if(orderService.updateOrder(orderId)){
                    System.out.println("Order " + orderId + " processed with status: Processed");
                }
                System.out.println("Order " + orderId + " is invalid");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

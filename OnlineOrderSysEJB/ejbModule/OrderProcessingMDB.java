import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.*;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/queue/OrderProcessingQueue"),
                @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "status = 'Pending'")
        }
)
public class OrderProcessingMDB implements MessageListener {
    @Inject
    private OrderService orderService;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String[] parts = textMessage.getText().split(",");
                Integer orderId = Integer.parseInt(parts[0]);
                String status = parts[1];

                orderService.updateOrder(orderId);
                System.out.println("Order " + orderId + " processed with status: Processed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package producer;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;

@LocalBean
@Stateless
public class TopicProducer {
    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/jms/queue/colaConsumer")
    private Queue notificar;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String send(Message message) {
        try (Connection connection = connectionFactory.createConnection();
             //no transaccional, confirmacion de la recepcion del mensaje de forma automatica
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

             //creo productor de mensaje y le indico el destino del mismo.
             MessageProducer messageProducer = session.createProducer(this.notificar)) {

            //indico al proveedor que persista los mensajes para que no se pierdan en caso de que se caiga el proveedor
            //si el proveedor JMS falla, el mensaje no se pierde y no se enviara dos veces.
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //envio el mensaje
            messageProducer.send(message);
            System.out.println("ENVIANDO A SUSCRIPTORES...");
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return "OK";
    }
}

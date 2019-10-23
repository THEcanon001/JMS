package service;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@LocalBean
@Path("")
public class ApiServiceProducer {
    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

   @Resource(mappedName = "java:/jms/queue/cola")
   private Queue notificarCola;

   @Resource(mappedName = "java:/jms/topic/topico")
   private Topic notificarTopic;

    @GET
    @Path("welcome")
    @Produces({MediaType.APPLICATION_JSON})
    public String status() {
        return "Tamo Activos...";
    }

    @GET
    @Path("broadcast")
    @Produces({MediaType.APPLICATION_JSON})
    public String broadcast(){
        try {
            sendMessage();
            sendMessageTopic();
            return "Todo salio bien";
        } catch (Exception e){
            e.printStackTrace();
            return "ERROR "+ e.getMessage();
        }
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void sendMessage() {
        try (Connection connection = connectionFactory.createConnection();
             //no transaccional, confirmacion de la recepcion del mensaje de forma automatica
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

             //creo productor de mensaje y le indico el destino del mismo.
             MessageProducer messageProducer = session.createProducer(this.notificarCola)) {

            //indico al proveedor que persista los mensajes para que no se pierdan en caso de que se caiga el proveedor
            //si el proveedor JMS falla, el mensaje no se pierde y no se enviara dos veces.
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //creo el mensaje
            TextMessage message = session.createTextMessage();
            message.setText("Mi primer cola JMS");
            message.setStringProperty("PROPERTY", "QUEUE");
            //envio el mensaje
            messageProducer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void sendMessageTopic() {
        try (Connection connection = connectionFactory.createConnection();
             //no transaccional, confirmacion de la recepcion del mensaje de forma automatica
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

             //creo productor de mensaje y le indico el destino del mismo.
             MessageProducer messageProducer = session.createProducer(this.notificarTopic)) {

            //indico al proveedor que persista los mensajes para que no se pierdan en caso de que se caiga el proveedor
            //si el proveedor JMS falla, el mensaje no se pierde y no se enviara dos veces.
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //creo el mensaje
            TextMessage message = session.createTextMessage();
            message.setText("Mi primer topic JMS");
            message.setStringProperty("PROPERTY", "TOPIC");
            //envio el mensaje
            messageProducer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

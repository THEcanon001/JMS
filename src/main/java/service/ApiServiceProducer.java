package service;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Singleton
@LocalBean
@Path("")
public class ApiServiceProducer {
    int count = 0;

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
    public String broadcast(String jsonScans){
        try {
            sendMessage(jsonScans);
            sendMessageTopic(jsonScans);
            return "Todo salio bien";
        } catch (Exception e){
            e.printStackTrace();
            return "ERROR "+ e.getMessage();
        }
    }


    @POST
    @Path("recive")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
//    public Response test(String jsonScans){
    public Response test(List<Map<String, Object>> eventoList){
        try {
            //broadcast(jsonScans);

            //System.out.println("El scan es :" +jsonScans);
            count++;
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
            System.out.println("CANTIDAD: "+count);
            System.out.println("-------------------------------------");
            System.out.println("-------------------------------------");
            return Response.status(200).entity("jsonScans").build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
        }
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void sendMessage(String jsonScans) {
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
            message.setText("Evo me volvio a mandar: "+jsonScans);
            message.setStringProperty("PROPERTY", "QUEUE");
            //envio el mensaje
            messageProducer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void sendMessageTopic(String jsonScans) {
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
            message.setText("Evo me mando: "+jsonScans);
            message.setStringProperty("PROPERTY", "TOPIC");
            //envio el mensaje
            messageProducer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

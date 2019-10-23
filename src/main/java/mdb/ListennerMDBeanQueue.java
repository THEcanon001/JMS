package mdb;


import producer.QueueProducer;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = "colaTutanka", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/cola"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "10") })

public class ListennerMDBeanQueue implements MessageListener {

    @EJB
    QueueProducer queueProducer;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println(textMessage.getText());
            queueProducer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

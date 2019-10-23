package mdb;

import producer.TopicProducer;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = "topic", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/topico"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "10"),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "clientID", propertyValue = "ListennerMDBeanTopic"),
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "ListennerMDBeanTopic")
})

public class ListennerMDBeanTopic implements MessageListener {
    @EJB
    TopicProducer topicProducer;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println(textMessage.getText());
            topicProducer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

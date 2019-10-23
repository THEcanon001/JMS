package mdb;


import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@MessageDriven(mappedName = "colaConsumer", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/colaConsumer"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "10") })

public class ListennerConsumer implements MessageListener {
        @Override
        public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage) message;
            //for(long i = 0; i < 10;i++) {
                try {
                    // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.
                    String remitente = "NotificacionesBCU";  //Para la dirección nomcuenta@gmail.com
                    String clave = "bcubcubcu";
                    Properties props = System.getProperties();
                    props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
                    props.put("mail.smtp.user", remitente);
                    props.put("mail.smtp.clave", clave);    //La clave de la cuenta
                    props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
                    props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
                    props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

                    javax.mail.Session session = javax.mail.Session.getDefaultInstance(props);
                    MimeMessage mensaje = new MimeMessage(session);
                    String mails_to_send = "ignaciod@correo.com.uy;christiang@correo.com.uy";
                    String mails[] = mails_to_send.split(";");
                    try {
                        mensaje.setFrom(new InternetAddress(remitente));
                        for (String mail : mails) {
                            Address destinatario = new InternetAddress(mail);
                            mensaje.addRecipient(javax.mail.Message.RecipientType.TO, destinatario);   //Se podrían añadir varios de la misma manera
                        }
                        String subject = "Alerta de seguridad";
                        String body = textMessage.getText() + " - " + textMessage.getStringProperty("PROPERTY");
                        body += "\nEste es un mensaje automatico, por favor no responder.";
                        mensaje.setSubject(subject);
                        mensaje.setText(body);
                        Transport transport = session.getTransport("smtp");
                        transport.connect("smtp.gmail.com", remitente, clave);
                        transport.sendMessage(mensaje, mensaje.getAllRecipients());
                        transport.close();
                    } catch (MessagingException me) {
                        me.printStackTrace();   //Si se produce un error
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
              //  System.out.println("Se envia alerta " + i);
           // }
        }
}

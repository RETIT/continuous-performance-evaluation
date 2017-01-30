package net.java.cargotracker.infrastructure.messaging.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.jms.Message;
import javax.jms.MessageListener;

@javax.ejb.MessageDriven(activationConfig = {
@ActivationConfigProperty(propertyName = "destinationType",
      propertyValue = "javax.jms.Queue"),
@ActivationConfigProperty(propertyName = "destinationLookup",
      propertyValue = "java:jboss/exported/jms/DeliveredCargoQueue")  
}, mappedName = "DeliveredCargoQueue")  
public class SimpleLoggingConsumer implements MessageListener {

    private static final Logger logger = Logger.getLogger(
            SimpleLoggingConsumer.class.getName());

    @Override
    public void onMessage(Message message) {
        logger.log(Level.FINE, "Received JMS message: {0}", message);
    }
}
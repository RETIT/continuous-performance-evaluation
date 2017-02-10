package net.java.cargotracker.infrastructure.messaging.jms;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.java.cargotracker.application.ApplicationEvents;
import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.handling.HandlingEvent;
import net.java.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

@ApplicationScoped
public class JmsApplicationEvents implements ApplicationEvents,Serializable {

	private static final long serialVersionUID = -4997907111126507474L;
	private static final int NORMAL_PRIORITY = 4;
	
    @Resource(lookup = "java:jboss/exported/jms/CargoHandledQueue")
    private Destination cargoHandledQueue;
    @Resource(lookup = "java:jboss/exported/jms/MisdirectedCargoQueue")
    private Destination misdirectedCargoQueue;
    @Resource(lookup = "java:jboss/exported/jms/DeliveredCargoQueue")
    private Destination deliveredCargoQueue;
    @Resource(lookup = "java:jboss/exported/jms/HandlingEventRegistrationAttemptQueue")
    private Destination handlingEventQueue;
    private static final Logger logger = Logger.getLogger(JmsApplicationEvents.class.getName());

    @Inject
	private Instance<JMSContext> jmsCtx;
    
    private void init(){
    	if(cargoHandledQueue!=null && misdirectedCargoQueue!=null && deliveredCargoQueue!=null && handlingEventQueue!=null){
    		return;
    	}
    	 try {
	    	Properties props = new Properties();
			
	        props.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
	
	        // The URL below should point to the your instance of Server 1, if no
	        // port offset is used for Server 1 the port can remain at 4447
	        props.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
	        // Credentials are required when the security is enabled (default behavior) on the HornetQ server 
	        props.put(Context.SECURITY_PRINCIPAL, "jmsuser");
	        props.put(Context.SECURITY_CREDENTIALS, "jmsuser");
	        
			InitialContext ic = new InitialContext(props);
			
	    	cargoHandledQueue = (Destination) ic.lookup("jms/CargoHandledQueue");
			misdirectedCargoQueue = (Destination) ic.lookup("jms/MisdirectedCargoQueue");
			deliveredCargoQueue = (Destination) ic.lookup("jms/DeliveredCargoQueue");
			handlingEventQueue = (Destination) ic.lookup("jms/HandlingEventRegistrationAttemptQueue");
    	 } catch (NamingException e) {
    		 if(logger.isLoggable(Level.SEVERE))
    			 logger.log(Level.SEVERE,"Could not estable JNDI Connection for JMS: ", e);
		}
    }
	
    private void sendJMSMessage(Cargo cargo, Destination destination){
    	init();
    	jmsCtx.get().createProducer()
        .setPriority(NORMAL_PRIORITY)
        .setDisableMessageID(true)
        .setDisableMessageTimestamp(true)
        .send(destination,
                cargo.getTrackingId().getIdString());
    	
    	if(logger.isLoggable(Level.FINEST))
    		logger.log(Level.FINEST,"SENT JMS MESSAGE TO " + destination);
    }
    
    @Override
    public void cargoWasHandled(HandlingEvent event) {
        Cargo cargo = event.getCargo();
        logger.log(Level.FINE, "Cargo was handled {0}", cargo);
        sendJMSMessage(cargo, cargoHandledQueue);
    }

    @Override
    public void cargoWasMisdirected(Cargo cargo) {
    	logger.log(Level.FINE, "Cargo was misdirected {0}", cargo);
        sendJMSMessage(cargo, misdirectedCargoQueue);
    }

    @Override
    public void cargoHasArrived(Cargo cargo) {
        logger.log(Level.FINE, "Cargo has arrived {0}", cargo);
        sendJMSMessage(cargo, deliveredCargoQueue);
    }
    
    @Override
    public void receivedHandlingEventRegistrationAttempt(HandlingEventRegistrationAttempt attempt) {
    	init();
        logger.log(Level.FINE, "Received handling event registration attempt {0}", attempt);

        jmsCtx.get().createProducer()
                .setPriority(NORMAL_PRIORITY)
                .setDisableMessageID(true)
                .setDisableMessageTimestamp(true)
                .send(handlingEventQueue, attempt);
        
        if(logger.isLoggable(Level.FINEST))
    		logger.log(Level.FINEST,"SENT JMS MESSAGE TO " + handlingEventQueue);
    }
}
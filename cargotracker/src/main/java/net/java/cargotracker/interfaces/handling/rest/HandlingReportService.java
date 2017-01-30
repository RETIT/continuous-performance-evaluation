package net.java.cargotracker.interfaces.handling.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import net.java.cargotracker.application.ApplicationEvents;
import net.java.cargotracker.application.BookingService;
import net.java.cargotracker.domain.model.cargo.Itinerary;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.handling.HandlingEvent;
import net.java.cargotracker.domain.model.location.UnLocode;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;
import net.java.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import net.java.cargotracker.interfaces.booking.facade.dto.RouteCandidate;
import net.java.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;


/**
 * This REST endpoint implementation performs basic validation and parsing of
 * incoming data, and in case of a valid registration attempt, sends an
 * asynchronous message with the information to the handling event registration
 * system for proper registration.
 */
@Stateless // TODO Make this a stateless bean for better scalability.
@Path("/handling")
public class HandlingReportService {

    public static final String ISO_8601_FORMAT = "yyyy-MM-dd HH:mm";
    public static final ThreadLocal<SimpleDateFormat> DATEFORMAT_THREADLOCAL = new ThreadLocal<>();
    @Inject
    private ApplicationEvents applicationEvents;
    @Inject
    private BookingService bookingService;
    
    public HandlingReportService() {}
    
    @POST
    @Path("/reports")
    @Consumes(MediaType.APPLICATION_JSON)
    // TODO Better exception handling.
    public void submitReport(@NotNull @Valid HandlingReport handlingReport) {
        try {
        	SimpleDateFormat dateFormat = DATEFORMAT_THREADLOCAL.get();
        	if(dateFormat == null) {
        		dateFormat = new SimpleDateFormat(ISO_8601_FORMAT, Locale.ENGLISH);
        		DATEFORMAT_THREADLOCAL.set(dateFormat);
        	}
			
            Date completionTime = dateFormat.parse(handlingReport.getCompletionTime());
            VoyageNumber voyageNumber = null;

            if (handlingReport.getVoyageNumber() != null) {
                voyageNumber = new VoyageNumber(
                        handlingReport.getVoyageNumber());
            }
            
            HandlingEvent.Type type = HandlingEvent.Type.valueOf(
                    handlingReport.getEventType());
            UnLocode unLocode = new UnLocode(handlingReport.getUnLocode());

            TrackingId trackingId = new TrackingId(handlingReport.getTrackingId());

            Date registrationTime = new Date();
            if(!type.equals(HandlingEvent.Type.UNLOAD) && !type.equals(HandlingEvent.Type.CLAIM)){
            	for(int i=1; i < (Math.random()*2);i++){
                	List<Itinerary> candidates = bookingService.requestPossibleRoutesForCargo(trackingId);
                	if(candidates!=null && !candidates.isEmpty()){
                		registrationTime = new Date();
                	}
                }	
            }
            
            HandlingEventRegistrationAttempt attempt =
                    new HandlingEventRegistrationAttempt(registrationTime,
                    completionTime, trackingId, voyageNumber, type, unLocode);

            applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);
        } catch (ParseException ex) {
            throw new RuntimeException("Error parsing completion time", ex);
        }
    }
}
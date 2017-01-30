package net.java.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DTO for a leg in an itinerary.
 */
public class Leg implements Serializable {

	// Needs to be a ThreadLocal because SimpleDateFormat is not thread-safe
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<>();

    private final String voyageNumber;
    private final String fromUnLocode;
    private final String fromName;
    private final String toUnLocode;
    private final String toName;
    private final String loadTime;
    private final String unloadTime;

    public Leg(
            String voyageNumber,
            String fromUnLocode,
            String fromName,
            String toUnLocode,
            String toName,
            Date loadTime,
            Date unloadTime) {
        this.voyageNumber = voyageNumber;
        this.fromUnLocode = fromUnLocode;
        this.fromName = fromName;
        this.toUnLocode = toUnLocode;
        this.toName = toName;
        SimpleDateFormat dateFormat = DATE_FORMAT.get();
        if(dateFormat == null) {
        	dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
        	DATE_FORMAT.set(dateFormat);
        }
        this.loadTime = dateFormat.format(loadTime);
        this.unloadTime = dateFormat.format(unloadTime);
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public String getFrom() {
        return fromName + " (" + fromUnLocode + ")";
    }

    public String getFromUnLocode() {
        return fromUnLocode;
    }

    public String getTo() {
        return toUnLocode + " (" + toName + ")";
    }

    public String getToUnLocode() {
        return toUnLocode;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public String getUnloadTime() {
        return unloadTime;
    }

    @Override
    public String toString() {
        return "Leg{" + "voyageNumber=" + voyageNumber + ", from=" + fromUnLocode + ", to=" + toUnLocode + ", loadTime=" + loadTime + ", unloadTime=" + unloadTime + '}';
    }
}

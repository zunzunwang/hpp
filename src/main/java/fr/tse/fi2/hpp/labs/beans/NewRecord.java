package fr.tse.fi2.hpp.labs.beans;

/**
 * Immutable Bean that maps (with almost no processing) a CSV record. The only
 * processing is the pickup/dropoff dates being transformed into unix timestamps.
 * 
 * <pre>
 * medallion	an md5sum of the identifier of the taxi - vehicle bound
 * hack_license	an md5sum of the identifier for the taxi license
 * pickup_datetime	time when the passenger(s) were picked up
 * dropoff_datetime	time when the passenger(s) were dropped off
 * trip_time_in_secs	duration of the trip
 * trip_distance	trip distance in miles
 * pickup_longitude	longitude coordinate of the pickup location
 * pickup_latitude	latitude coordinate of the pickup location
 * dropoff_longitude	longitude coordinate of the drop-off location
 * dropoff_latitude	latitude coordinate of the drop-off location
 * payment_type	the payment method - credit card or cash
 * fare_amount	fare amount in dollars
 * surcharge	surcharge in dollars
 * mta_tax	    tax in dollars
 * tip_amount	tip in dollars
 * tolls_amount	bridge and tunnel tolls in dollars
 * total_amount	total paid amount in dollars
 * 
 * </pre>
 * 
 * @author Julien
 */
public class NewRecord {

	/** time when the passenger(s) were picked up. */
	final long pickup_datetime;

	/** time when the passenger(s) were dropped off. */
	final long dropoff_datetime;
/*
	final int pickup_longitude_grid;
	final int pickup_latitude_grid;
	final int dropoff_longitude_grid;
	final int dropoff_latitude_grid;
*/	
	final GridPoint pickup ;
	
	final GridPoint dropoff;
	
	/** times of trip. */
	final int times;	
	


	public NewRecord(long pickup_datetime, long dropoff_datetime,
			GridPoint pickup, GridPoint dropoff, int times) {
		super();
		this.pickup_datetime = pickup_datetime;
		this.dropoff_datetime = dropoff_datetime;
		this.pickup = pickup;
		this.dropoff = dropoff;
		this.times = times;

	}

	/**
	 * @return the pickup_datetime
	 */
	public final long getPickup_datetime() {
		return pickup_datetime;
	}

	/**
	 * @return the dropoff_datetime
	 */
	public final long getDropoff_datetime() {
		return dropoff_datetime;
	}

	/**
	 * @return the point pick up
	 */
	public  GridPoint getPickup(){
		return pickup;
	}
	
	/**
	 * @return the point drop off
	 */
	public GridPoint getDropoff(){
		return dropoff;
	}
	
	/**
	 * @return the times
	 */
	public final int getTimes() {
		return times;
	}
	


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
			return "DebsRecord [pickup_datetime=" + pickup_datetime
					+ ", dropoff_datetime=" + dropoff_datetime
					+ ", pickup" + pickup
					+ ", pickup=" + dropoff
					+ ", times=" + times
				    + "]";
		
	}

}
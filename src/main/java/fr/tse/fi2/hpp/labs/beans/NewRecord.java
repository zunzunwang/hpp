package fr.tse.fi2.hpp.labs.beans;
/**
 * On créer une classe nouvelle pour sauvegarder le record qui contient
 * pick_up time
 * drop_off time
 * pickup(grid point)
 * dropoff(grid point)
 * times  
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
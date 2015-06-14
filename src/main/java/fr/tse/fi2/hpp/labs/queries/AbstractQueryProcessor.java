package fr.tse.fi2.hpp.labs.queries;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.NewRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.*;
import fr.tse.fi2.hpp.labs.queries.impl.lab3_zunzunwang.WriteResultat;

/**
 * Every query must extend this class that provides basic functionalities such
 * as :
 * <ul>
 * <li>Receives event from {@link StreamingDispatcher}</li>
 * <li>Notify start/end time</li>
 * <li>Manages thread synchronization</li>
 * <li>Grid mapping: maps lat/long to x,y in a discrete grid of given size</li>
 * </ul>
 * 
 * @author Julien
 * 
 */
public abstract class AbstractQueryProcessor implements Runnable {

	final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);

	/**
	 * Counter to uniquely identify the query processors
	 */
	private final static AtomicInteger COUNTER = new AtomicInteger();
	/**
	 * Unique ID of the query processor
	 */
	private final int id = COUNTER.incrementAndGet();
	/**
	 * Writer to write the output of the queries
	 */
//	private BufferedWriter outputWriter;//écrire sous buffer
	/**
	 * Internal queue of events
	 */
	public final BlockingQueue<DebsRecord> eventqueue;
	
	
	
	/**
	 * Internal queue of result
	 */
	public final BlockingQueue<String> resultqueue;
	/**
	 * Global measurement
	 */
	private final QueryProcessorMeasure measure;
	/**
	 * For synchronisation purpose
	 */
	private CountDownLatch latch;
	
	private Thread thread;

	/**
	 * Default constructor. Initialize event queue and writer
	 */
	public AbstractQueryProcessor(QueryProcessorMeasure measure) {
		// Set the global measurement instance
		this.measure = measure;
		// Initialize queue
		this.eventqueue = new LinkedBlockingQueue<>();
		this.resultqueue = new LinkedBlockingQueue<>();
		WriteResultat result = new WriteResultat(id, resultqueue);
		this.thread= new Thread(result);
		thread.setName("QPWriter"+id);
		thread.start();

		// Initialize writer: écrire sous buffer ici on utilise writeResultat pour écrire
		/*try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/query" + id + ".txt")));
		} catch (IOException e) {
			logger.error("Cannot open output file for " + id, e);
			System.exit(-1);
		}*/
		
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override//run programme dans chaque process
	public void run() {
		logger.info("Starting query processor " + id);
		// Notify beginning of processing
		measure.notifyStart(this.id);
		while (true) {
			try {
				DebsRecord record = eventqueue.take();
				if (record.isPoisonPill()) {
					break;
				} else {
					process(record);
				}
			} catch (InterruptedException e) {
				logger.error(
						"Error taking element from internal queue, processor "
								+ id, e);
				break;
			}
		}
		// Finish, close the writer and notify the measurement
		finish();
		logger.info("Closing query processor " + id);
	}

	/**
	 * 
	 * @param record
	 *            record to be processed
	 */
	protected abstract void process(DebsRecord record);

	/**
	 * 
	 * @param record
	 *            the record to process
	 * @return the route in a 600*600 grid
	 */
	protected Route convertRecordToRoute(DebsRecord record) {
		// Convert pickup coordinates into cell
		float lat1 = record.getPickup_latitude();
		float long1 = record.getPickup_longitude();
		GridPoint pickup = convert(lat1, long1);
		// Convert pickup coordinates into cell
		float lat2 = record.getDropoff_latitude();
		float long2 = record.getDropoff_longitude();
		GridPoint dropoff = convert(lat2, long2);
		return new Route(pickup, dropoff);
	}
	
	//Récrire une nouvelle fonction pour convertir NewRecord
	protected NewRecord convertRecordToNewRoute(DebsRecord record) {
		NewRecord newRecord ; 
		// the time
		long pickup_datatime = record.getPickup_datetime();
		long dropoff_datatime = record.getDropoff_datetime();
		// Convert pickup coordinates of new record into cell
		float long1 = record.getPickup_longitude();
		float lat1 = record.getPickup_latitude();
		// Convert pickup coordinates of new record into cell
		float long2 =record.getDropoff_longitude();
		float lat2 =record.getDropoff_latitude();
		//the times
		int times = 0;
		GridPoint pickup = convert(lat1, long1);
		GridPoint dropoff = convert(lat2, long2);
		newRecord = new NewRecord(pickup_datatime, dropoff_datatime, pickup, dropoff, times);
		return newRecord;

	}
	
	// Query2 et unité est 250m. 
		protected String convertTounit(float latitude, float longitude)
		{
			// longueur et largeur de chaque unité.
			double stepX = 0.005986/2;
			double stepY = 0.004491556/2;
			
			// le point du début. 
			double startX = -74.913585;
			double startY = 41.474937;
			
			// la normalisation.
			double unitX = (longitude - startX) / stepX;
			double unitY = (startY - latitude) / stepY;
			Integer X = (int) (Math.round(unitX) + 1);
			Integer Y = (int) (Math.round(unitY) + 1);
			String result = X.toString() + "." + Y.toString();
			return result;
		}



	/**
	 * 
	 * @param lat1
	 * @param long1
	 * @return The lat/long converted into grid coordinates
	 */
	private GridPoint convert(float lat1, float long1) {
		return new GridPoint(cellX(long1), cellY(lat1));
	}

	/**
	 * Provided by Syed and Abderrahmen
	 * 
	 * @param x
	 * @return
	 */
	private int cellX(float x) {

		// double x=0;
		double x_0 = -74.913585;
		double delta_x = 0.005986;// / 2;

		// double cell_x;
		Double cell_x = 1 + Math.floor(((x - x_0) / delta_x) + 0.5);

		return cell_x.intValue();
	}

	/**
	 * Provided by Syed and Abderrahmen
	 * 
	 * @param y
	 * @return
	 */
	private int cellY(double y) {

		double y_0 = 41.474937;
		double delta_y = 0.004491556;// / 2;

		Double cell_y = 1 + Math.floor(((y_0 - y) / delta_y) + 0.5);

		return cell_y.intValue();

	}

	/**
	 * @return the id of the query processor
	 */
	public final int getId() {
		return id;
	}

	/**
	 * 
	 * @param line
	 *            the line to write as an answer
	 */
	protected void writeLine(String line) {
		/*try {
			outputWriter.write(line);
			outputWriter.newLine();
		} catch (IOException e) {
			logger.error("Could not write new line for query processor " + id
					+ ", line content " + line, e);
		}
*/
		System.out.println(line);
		resultqueue.add(line);
		
	}
	
	
	
	

	/**
	 * Poison pill has been received, close output
	 */
	protected void finish() {
		// Close writer
	/*	try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			logger.error("Cannot property close the output file for query "
					+ id, e);
		}*/
		// Notify finish time
		measure.notifyFinish(this.id);
		resultqueue.add("DIE!!!");
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Decrease latch count
		latch.countDown();


	}
	



}

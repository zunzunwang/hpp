package fr.tse.fi2.hpp.labs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.LoadFirstDispatcher;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.SimpleQuerySumEvent;
import fr.tse.fi2.hpp.labs.queries.impl.lab1_zunzunwang.AverageQuery;
import fr.tse.fi2.hpp.labs.queries.impl.lab1_zunzunwang.IncrementalAverage;
import fr.tse.fi2.hpp.labs.queries.impl.lab1_zunzunwang.StupidAveragePrice;
import fr.tse.fi2.hpp.labs.queries.impl.lab1_zunzunwang.SumQuery;
import fr.tse.fi2.hpp.labs.queries.impl.lab4_zunzunwang.RouteMembershipProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.lab4_zunzunwang.SimpleBloomFilter;
import fr.tse.fi2.hpp.labs.queries.impl.projet.AreasProfitable;
import fr.tse.fi2.hpp.labs.queries.impl.projet.RoutesFrequente;

/**
 * Main class of the program. Register your new queries here
 * 
 * Design choice: no thread pool to show the students explicit
 * {@link CountDownLatch} based synchronization.
 * 
 * @author Julien
 * 
 */
public class MainNonStreaming {
	
	static String recordTest=null;

	final static Logger logger = LoggerFactory
			.getLogger(MainNonStreaming.class);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher and load everything
		LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
				"src/main/resources/data/1000Records.csv");
		
//		LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
//				"src/main/resources/data/sorted_data.csv");
//		LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
//				"src/main/resources/data/test_02.csv");
		
		
		logger.info("Finished parsing");
		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		// Add you query processor here
		
//		processors.add(new StupidAveragePrice(measure));
//		processors.add(new AverageQuery(measure));
//		processors.add(new IncrementalAverage(measure));//我们加入了不同的线程
//		processors.add(new RouteMembershipProcessor(measure));
//		processors.add(new SimpleBloomFilter(measure));
//		processors.add(new Query1(measure));
		processors.add(new RoutesFrequente(measure));
		processors.add(new AreasProfitable(measure));

		

		
		
		
		// Register query processors
		for (AbstractQueryProcessor queryProcessor : processors) {
			dispatch.registerQueryProcessor(queryProcessor);
		}
		// Initialize the latch with the number of query processors
		CountDownLatch latch = new CountDownLatch(processors.size());
		// Set the latch for every processor
		for (AbstractQueryProcessor queryProcessor : processors) {
			queryProcessor.setLatch(latch);
		}
		for (AbstractQueryProcessor queryProcessor : processors) {
			Thread t = new Thread(queryProcessor);
			t.setName("QP" + queryProcessor.getId());
			t.start();
		}
		// Start everything dispatcher first, not as a thread
		dispatch.run();
		logger.info("Finished Dispatching");
		// Wait for the latch
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error while waiting for the program to end", e);
		}
		// Output measure and ratio per query processor
		measure.setProcessedRecords(dispatch.getRecords());
		measure.outputMeasure();
//		DebsRecord record;
//		record = RouteMembershipProcessor.getRecord();
		
//		System.out.print("recherche de route :" + RouteMembershipProcessor.checkroute(record));
//		recordTest = SimpleBloomFilter.getRecord();
//		System.out.print("recherche de route :" + SimpleBloomFilter.contains(recordTest));


	}

}

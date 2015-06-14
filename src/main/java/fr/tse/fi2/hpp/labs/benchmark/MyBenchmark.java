/*
package fr.tse.fi2.hpp.labs.benchmark;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.LoadFirstDispatcher;
import fr.tse.fi2.hpp.labs.main.MainNonStreaming;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.lab4_zunzunwang.RouteMembershipProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.lab4_zunzunwang.SimpleBloomFilter;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@BenchmarkMode(Mode.AverageTime)
//@OutputTimeUnit(TimeUnit.NANOSECONDS)
//@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)


@Fork(1)
@Measurement(iterations = 5)
@Warmup(iterations = 5)
@State(Scope.Benchmark)
//@State(Scope.Thread)

public class MyBenchmark {
//	private DebsRecord recordTest;//pour 1 version
	private String recordTest;
	
	final static Logger logger = LoggerFactory
			.getLogger(MainNonStreaming.class);
	

	
	@Setup//depends faire benchmark before or after

	public void init(){

			// Init query time measure
			QueryProcessorMeasure measure = new QueryProcessorMeasure();
			// Init dispatcher and load everything
			LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
					"src/main/resources/data/1000Records.csv");
			
			
//			LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
	//				"src/main/resources/data/sorted_data.csv");
			logger.info("Finished parsing");
			// Query processors
			List<AbstractQueryProcessor> processors = new ArrayList<>();
			// Add you query processor here
			
//			processors.add(new StupidAveragePrice(measure));
//			processors.add(new AverageQuery(measure));
//			processors.add(new IncrementalAverage(measure));//on ajoute process differentes
//			processors.add(new RouteMembershipProcessor(measure));
			processors.add(new SimpleBloomFilter(measure));

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
//			recordTest= RouteMembershipProcessor.getRecord();
			recordTest = SimpleBloomFilter.getRecord();
			


		
	}

    @Benchmark
    public void testMethod1() {

 //   	System.out.println("Route find : " + RouteMembershipProcessor.checkroute(recordTest));
    	System.out.println("Route find : " + SimpleBloomFilter.contains(recordTest));
    	
    	
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}

*/
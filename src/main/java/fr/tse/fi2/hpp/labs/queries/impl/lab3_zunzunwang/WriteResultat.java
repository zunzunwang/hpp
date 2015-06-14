package fr.tse.fi2.hpp.labs.queries.impl.lab3_zunzunwang;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
/**
 * 
 * Récrire la fonction de out_put. 
 *
 */
public class WriteResultat implements Runnable{
	
	private BufferedWriter outputWriter;//écrire sous buffer
	final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);
	private int id;
	public final BlockingQueue<String> resultqueue;


	public WriteResultat(int id,BlockingQueue<String> resultqueue) {
		super();
		this.id=id;
		// TODO Auto-generated constructor stub
		this.resultqueue =resultqueue;
		
		
		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/query" + id + ".txt")));
		} catch (IOException e) {
			logger.error("Cannot open output file for " + id, e);
			System.exit(-1);
		}
		
		
	}


	@Override
	public void run() {
		
		//recuperer la queue
		//ecrire
		while(true){
			try{
				String line = resultqueue.take();
				if(line.equals("DIE!!!")){
					break;
				}
				writeLine(line);
			}catch (InterruptedException e){
				e.printStackTrace();
			}					
		}
		finish();			
	}
	
	protected void writeLine(String line){
		try {
			outputWriter.write(line);
			outputWriter.newLine();
		} catch (IOException e) {
			logger.error("Could not write new line for query processor " + id
					+ ", line content " + line, e);
		}
	}
	
	
	
	public void finish(){
		//close writer
		try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			logger.error("Cannot property close the output file for query "
					+ id, e);
		}
		
	}

}

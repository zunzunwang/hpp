package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class ProfitableAreas extends AbstractQueryProcessor {
	private static LinkedList<DebsRecord> List_record_15min = new LinkedList<>();
	private static LinkedList<DebsRecord> List_record_30min = new LinkedList<>();
	private static long delay;
	private static long temps_debut;
	private static long temps_fin;
	private String result;	

	public ProfitableAreas(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		List_record_15min.add(record);
		if (List_record_15min.size()>0)
		{ 
			
		}

	}

}

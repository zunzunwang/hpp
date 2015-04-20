package fr.tse.fi2.hpp.labs.queries.impl.lab1_zunzunwang;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class AverageQuery extends AbstractQueryProcessor {
	private float sum=0;
	private float ave=0;
	private float distance=0;

	public AverageQuery(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		sum+=record.getFare_amount();
		distance+=record.getTrip_distance();
		ave=sum/distance;
		writeLine("ave"+ave);

	}

}

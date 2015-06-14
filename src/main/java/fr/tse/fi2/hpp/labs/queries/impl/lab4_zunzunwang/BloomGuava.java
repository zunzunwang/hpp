package fr.tse.fi2.hpp.labs.queries.impl.lab4_zunzunwang;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class BloomGuava extends AbstractQueryProcessor {
	
	private static BloomFilter<DebsRecord> rec;
	
	public BloomGuava(QueryProcessorMeasure measure) {
		super(measure);
		rec = BloomFilter.create(recFunnel, 1000, 0.001);
		// TODO Auto-generated constructor stub
	}
	
	Funnel<DebsRecord> recFunnel = new Funnel<DebsRecord>() {
		@Override
	     public void funnel(DebsRecord record, PrimitiveSink into) {
	       into.putFloat(record.getPickup_latitude())
	           .putFloat(record.getPickup_longitude())
	           .putFloat(record.getDropoff_latitude())
	           .putFloat(record.getDropoff_longitude())
	           .putString(record.getHack_license(), Charsets.UTF_8);
	     }
	   };
	
	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		for (DebsRecord debsRecord : eventqueue) {
			rec.put(debsRecord);
		}
	}
	
	public static int check(DebsRecord recherche)
	{
		int nb = 0;
		if (rec.mightContain(recherche)) {
			nb ++;
			}
		return nb;
	}

}
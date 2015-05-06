package fr.tse.fi2.hpp.labs.queries.impl.lab4_zunzunwang;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor extends AbstractQueryProcessor {
/*	
	private float x1=0;
	private float y1=0;
	private float x2=0;
	private float y2=0;
	*/
	private String licence=null;	
	private static ArrayList<DebsRecord> ListeRoute = new ArrayList<DebsRecord>();
	private static DebsRecord recordTest;
	private int compte=0;
	
	

	public RouteMembershipProcessor(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub	

			ListeRoute.add(record);
			compte++;
			if(compte==20){
					recordTest = record;
			}		
			
			
		}
	public static DebsRecord getRecord(){
		return recordTest;
	}
	
	public static boolean checkroute(DebsRecord record)
	{
		for(int i=0;i<ListeRoute.size();i++){
			if(record.getPickup_longitude()== ListeRoute.get(i).getPickup_longitude()
			   && record.getPickup_latitude()== ListeRoute.get(i).getPickup_latitude()
			   &&record.getDropoff_longitude()== ListeRoute.get(i).getDropoff_longitude()
			   && record.getDropoff_latitude()== ListeRoute.get(i).getDropoff_latitude()
			   && record.getHack_license()== ListeRoute.get(i).getHack_license()
			   )
			{
				return true;
			}
				
		}
		return false;
	}

}


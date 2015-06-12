package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query1 extends AbstractQueryProcessor {
    
	private List<DebsRecord> recordList;
	private Multiset<Route> top10;
	private List<DebsRecord> recordList_tri;
	private Multiset<DebsRecord> recordList_tri_2;
	private ComparateurList comparatorlist;


	private String resultat;
	private long tempsCommence;
	private long tempsFini;
	private long delay;

		
	public Query1(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
		this.recordList = new LinkedList<DebsRecord>();
		this.recordList_tri =new LinkedList<DebsRecord>();
		this.recordList_tri_2 =HashMultiset.create();
		this.comparatorlist = new ComparateurList();
	}
	
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub


    	this.top10 = HashMultiset.create();	
		recordList.add(record);

		
		
		//Obtenir le temps de 'pickup' et 'dropoff' updated;
		Date temps_pickup = new Date(record.getPickup_datetime());
		Date temps_dropoff = new Date(record.getDropoff_datetime());
		Format temps_outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		resultat = temps_outputformat.format(temps_pickup)+','+temps_outputformat.format(temps_dropoff);
		
		
		//Obtenir la liste dans la domaine [a-30,a] a:la temps le plus tard;
		if (recordList.size() > 0)
		{
			tempsCommence = System.nanoTime();
			if (record.getDropoff_datetime() >= (recordList.get(0).getDropoff_datetime()+1800000))//30min et i'unite est ms
			{
				
				for (int i=0; i<recordList.size(); i++) {
				
					if(recordList.get(i).getDropoff_datetime() <= record.getDropoff_datetime()-1800000)
						recordList.remove(i);
				
				}
			}
			
			
			
			recordList_tri.clear();
			recordList_tri_2.clear();
			for (DebsRecord indexRecord : recordList){
				recordList_tri_2.add(indexRecord);
			} 
			ImmutableMultiset<DebsRecord> highestCountFirst = Multisets.copyHighestCountFirst(recordList_tri_2);
			for (DebsRecord indexRecord : highestCountFirst){
				recordList_tri.add( indexRecord);
			}			
			recordList_tri=comparatorlist.freshList(recordList_tri);
			
			top10.clear();
			for (DebsRecord indexRecord : recordList_tri){
				Route indexRoute = convertRecordToRoute(indexRecord);
				top10.add(indexRoute);				
			} 
			
			
			//Ajouter les routes de top10 à resultat, si moins de 10, ajouter 'NULL'; 
			int i=1;
			for(Route route : top10 ){
				if(i<11){
					
					resultat += ", " + route.getPickup().getX() + "." + route.getPickup().getY();
					resultat += ", " + route.getDropoff().getX() + "." + route.getDropoff().getY();
					
				}
				
				i++;
			}
			
			while(i<11){
				resultat += ", NULL, NULL";
				i++;
			}
			
		}
		
		//Calculer le delay;
		tempsFini = System.nanoTime();
		delay = tempsFini - tempsCommence;
		this.writeLine(resultat + ", " + delay);
	}
}



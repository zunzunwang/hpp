package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.NewRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang.InsertSort;

public class RoutesFrequente extends AbstractQueryProcessor {
    
	private List<DebsRecord> recordList;
	private Multiset<Route> listRoute;
	private List<NewRecord> newRecordList;
	private List<NewRecord> newRecordList2;
	private Multiset<Integer> timesList;
	private List<NewRecord> listResultat;


	private String resultat;
	private long tempsCommence;
	private long tempsFini;
	private long delay;

		
	public RoutesFrequente(QueryProcessorMeasure measure) {
 		super(measure);
		// TODO Auto-generated constructor stub
		this.recordList = new LinkedList<DebsRecord>();
//		this.listRoute = HashMultiset.create();
//		this.newRecordList = new LinkedList<NewRecord>();
//		this.newRecordList2 = new LinkedList<NewRecord>();
//		this.timesList = HashMultiset.create();
//		this.listResultat = new LinkedList<NewRecord>();
//		this.comparatorlist = new ComparatorList();
 	}
	
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		this.listRoute = HashMultiset.create();
		this.newRecordList = new LinkedList<NewRecord>();
		this.newRecordList2 = new LinkedList<NewRecord>();
		this.timesList = HashMultiset.create();
		this.listResultat = new LinkedList<NewRecord>();

  //  	this.listRoute = HashMultiset.create();	
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
			
			
			
			
			
			for(DebsRecord recordTest : recordList){
				newRecordList.add(convertRecordToNewRoute(recordTest));
//oui				System.out.println("asdasdsad"+convertRecordToNewRoute(recordTest).getDropoff().getX());
			}

			
			listRoute.clear();
			for (DebsRecord indexRecord : recordList){
 				Route indexRoute = convertRecordToRoute(indexRecord);
//				System.out.println("asdasdsad"+convertRecordToRoute(indexRecord).getDropoff().getX());
				listRoute.add(indexRoute);				
			} 
			
//			Set<Route> highestCountFirst = Multisets.copyHighestCountFirst(listRoute).elementSet();
			ImmutableMultiset<Route> highestCountFirst = Multisets.copyHighestCountFirst(listRoute);

//            System.out.println(highestCountFirst.size());
			for(Route route : highestCountFirst){
				int nTimes = 0;
				long timeStart = 0;
				long timeLast = 0;

				for(NewRecord newRecord : newRecordList){
	//				System.out.println("newrecordY+"+newRecord.getDropoff().getY());
	//				System.out.println("newrecordX+"+newRecord.getDropoff().getX());
		//			System.out.println("routeY+"+route.getDropoff().getY());
		//			System.out.println("routeX+"+route.getDropoff().getX());

					
					if(((route.getPickup()).getX()==(newRecord.getPickup().getX()))
							&&((route.getPickup()).getY()==(newRecord.getPickup().getY())
							&& (route.getDropoff().getX()==newRecord.getDropoff().getX())
							&&(route.getDropoff().getY()==newRecord.getDropoff().getY()))){
						nTimes += 1;
						if((new Date(newRecord.getDropoff_datetime()).compareTo(new Date(timeLast)))>0){
							timeStart = newRecord.getPickup_datetime();
							timeLast = newRecord.getDropoff_datetime();
						} 
					}
				}
//				System.out.println("je vais ajouter fuck"+newRecordList2.size());
				newRecordList2.add(new NewRecord(timeStart,timeLast,route.getPickup(),route.getDropoff(),nTimes));
                timesList.add(nTimes);
//            			System.out.println("times+++"+nTimes);
			}
//			for(NewRecord newrecord:newRecordList2){
//			System.out.println("NewrecordList2:"+newrecord.getTimes());
//		    }				
			
			

	//		System.out.println("size list2"+newRecordList2.size());
			ComparateurTime comtime =new ComparateurTime();
			newRecordList2=comtime.freshTime(newRecordList2);
/*
 * 检验newrecordlist2是否为降序
 */
//			for(NewRecord newrecord:newRecordList2){
//				System.out.println("NewrecordList2:"+newrecord.getTimes());
//			}	
			
			
			Set<Integer> timesList2 = Multisets.copyHighestCountFirst(timesList).elementSet();
			int[] timesList3 = new int[timesList2.size()] ;		
			int a=0;
			for(Integer index: timesList2){
				timesList3[a]=index;
				a++;
				}
			timesList3=InsertSort.InsertSort(timesList3);
/*
 *检验timelist3是否为降序			
 */
//			for(Integer index: timesList3){
//				System.out.println("TimeList3:"+index);
				
//			}			

			int nAll=0;
			for(Integer index: timesList3){
//				System.out.println(index);
				int n = 0;
				for(NewRecord newRecord : newRecordList2){
					if(index == newRecord.getTimes()){
						n += 1;
						nAll += 1;
					}	
				}
		//	    System.out.println(n);
		//	    System.out.println(nAll);				
				ComparateurList comList= new ComparateurList();
				listResultat = comList.freshList(newRecordList2, n, nAll);
				int c=0;
				for(NewRecord newRecord: listResultat ){
					System.out.println("getTIme: "+newRecord.getTimes()+"   getdropoff  "+temps_outputformat.format(new Date(newRecord.getDropoff_datetime())));
					c++;
					if (c>=10)
						break;
				}
				
				
				if(nAll >= 10){
					break;
				}

			}



			
			
			
			
			
			
			//Ajouter les routes de listRoute à resultat, si moins de 10, ajouter 'NULL'; 
			int i=1;
			for(NewRecord newRecord: listResultat ){

 				if(i<11){
					
					resultat += ", " + newRecord.getPickup().getX() + "." + newRecord.getPickup().getY();
					resultat += ", " + newRecord.getDropoff().getX() + "." + newRecord.getDropoff().getY();
					
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
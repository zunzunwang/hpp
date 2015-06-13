package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.NewRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang.InsertSort;

public class RoutesFrequente extends AbstractQueryProcessor {
    
	private List<DebsRecord> recordList;
	private LinkedList<Route> listRoute;
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
 	}
	
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		this.listRoute = new LinkedList<Route>();;
		this.newRecordList = new LinkedList<NewRecord>();
		this.newRecordList2 = new LinkedList<NewRecord>();
		this.timesList = HashMultiset.create();
		this.listResultat = new LinkedList<NewRecord>();
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
			
			
			
			
			for(int i=0;i<recordList.size();i++){
				newRecordList.add(convertRecordToNewRoute(recordList.get(i)));
			}						
/*			
			for(NewRecord newrecord:newRecordList){
				System.out.println("NewrecordList:"+newrecord.getTimes()
						+"  getdropoff time  "+temps_outputformat.format(new Date(newrecord.getDropoff_datetime()))
						+"  get pickup  "+newrecord.getPickup().getX()+newrecord.getPickup().getY()
						+"  get drop off "+newrecord.getDropoff().getX()+newrecord.getDropoff().getY());
			}	

*/			
			listRoute.clear();
			for (DebsRecord indexRecord : recordList){
 				Route indexRoute = convertRecordToRoute(indexRecord);
				listRoute.add(indexRoute);				
			} 			
			Set<Route> highestCountFirst =new  HashSet<Route>();
			highestCountFirst.clear();
			highestCountFirst.addAll(listRoute);
			List<Route> highestCountSecond = new LinkedList<Route>();
			highestCountSecond.clear();		
			highestCountSecond.addAll(highestCountFirst);

			List<Route> TriRoute = new LinkedList<Route>();
			TriRoute.clear();
			for(int i=newRecordList.size();i>0;i--){
				Route route = new Route (newRecordList.get(i-1).getPickup(),newRecordList.get(i-1).getDropoff());
				for(int j=0;j<highestCountSecond.size();j++){
					Route route1 = highestCountSecond.get(j);					
					if(     
							(route1.getPickup().getX())==(route.getPickup().getX())
							&& ((route1.getDropoff().getX())==(route.getDropoff().getX()))
						    && ((route1.getPickup().getY())==(route.getPickup().getY()))
						    && ((route1.getDropoff().getY())==(route.getDropoff().getY()))		   
							)
						    
					{
						TriRoute.add(route);						
		    			highestCountSecond.remove(route1);	
						break;
					}
				}
				

				if (highestCountFirst.isEmpty())
				break;	
			}
/*
			for(Route route : TriRoute){
				System.out.println("  get pickup  "+route.getPickup().getX()+route.getPickup().getY()
						+"  get drop off "+route.getDropoff().getX()+route.getDropoff().getY());
			}
*/

			for(int i=0;i<TriRoute.size();i++){
				Route route =TriRoute.get(i);				
				int nTimes = 0;
				long timeStart = 0;
				long timeLast = 0;

				for(NewRecord newRecord : newRecordList){					
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
				newRecordList2.add(new NewRecord(timeStart,timeLast,route.getPickup(),route.getDropoff(),nTimes));
                timesList.add(nTimes);
			}
/*			
			for(NewRecord newrecord:newRecordList2){
				System.out.println("NewrecordList2:"+newrecord.getTimes()
						+"  getdropoff time  "+temps_outputformat.format(new Date(newrecord.getDropoff_datetime()))
						+"  get pickup  "+newrecord.getPickup().getX()+newrecord.getPickup().getY()
						+"  get drop off "+newrecord.getDropoff().getX()+newrecord.getDropoff().getY());
			}
*/							
				
			Set<Integer> timesList2 = new HashSet<Integer>();
			timesList2.addAll(timesList);
			
			int[] timesList3 = new int[timesList2.size()] ;		
			int a=0;
			for(Integer index: timesList2){
				timesList3[a]=index;
				a++;
				}
			timesList3=InsertSort.InsertSort(timesList3);			
			
			//Ajouter les routes de listRoute à resultat, si moins de 10, ajouter 'NULL'; 
			int i=1;
			for(NewRecord newRecord: newRecordList2 ){

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
	
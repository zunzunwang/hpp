package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class AreasProfitable extends AbstractQueryProcessor {

	private long tempsCommence;
	private long tempsFini;
	private long delay;
	long tempsDroppffRecord ;
	final long duree15min = 900000;
	LinkedList<DebsRecord> listArea15;
	LinkedList<DebsRecord> list15;
	LinkedList<DebsRecord> listDropoff;
	HashMap<String, ListMedian> listAreaProfitable;
	public AreasProfitable(QueryProcessorMeasure measure) {
		super(measure);
		tempsDroppffRecord = 0;
		listArea15= new LinkedList<DebsRecord>();
		list15 = new LinkedList<DebsRecord>();
		listAreaProfitable = new HashMap<String, ListMedian>();
		listDropoff = new LinkedList<DebsRecord>();
	}

	@Override
	protected void process(DebsRecord record) {
		
		//Initialisation du temps actual utilisant le dropoff_datatime du record
		tempsDroppffRecord=record.getDropoff_datetime();
		String areaPickup  = convertTounit(record.getPickup_latitude(), record.getPickup_longitude());
		float fare = record.getFare_amount();
	    float tip  = record.getTip_amount();
	    if (fare >= 0 && tip >= 0)
	    {
	    	// Commencer.
	    	tempsCommence = System.nanoTime();
	    	list15.add(record);
	    	if (listAreaProfitable.containsKey(areaPickup))
	    	{
	    		listAreaProfitable.get(areaPickup).ajoute(fare + tip);
	    		}
	    	else
	    	{
	    		ListMedian tmp = new ListMedian();
	    		tmp.ajoute(fare + tip);
	    		listAreaProfitable.put(areaPickup, tmp);
	    		}
	    	}
	    supprimerRecord(tempsDroppffRecord - duree15min);
	  
		System.out.println("*********************************************"
						          + "*********************************************");
		// Sauvegarder les résultats .
		ArrayList<Areas> listArea = new ArrayList<Areas>();
		listArea15.add(record);
		// Selectionner les records dans 15min.
	    for(int i=0;i<listArea15.size();i++){
	    	if ((tempsDroppffRecord- listArea15.get(i).getPickup_datetime()) >duree15min){
	    		listArea15.remove(i);
	    		}
	    	}
	    for(int i=0;i<listArea15.size();i++){
	    	Areas area = new Areas();
	    	area.setunit(convertTounit(listArea15.get(i).getPickup_latitude() , listArea15.get(i).getPickup_longitude()));
	    	boolean find = false;
	    	
	    	/**
	    	 * vérifier si le 'area' est déjà  existé dans la liste.
	    	 * si il est existé. On ajoute le revenu dans la liste.
	    	 * sinon, on va crée un nouveau 'area' dans la liste.
	    	 */
	    	for(int j=0;j<listArea.size();j++){
	    		if(area.getunit().equals(listArea.get(j).getunit())){
	    			listArea.get(j).mediane.ajoute(fare + tip);
	    			listArea.get(j).mediane.getMediane();
	    			listArea.get(j).calculprofitability();
	    			find = true;
	    			}
	    		}
	    	
	    	if(!find){
	    		area.setNbTaxisEmpty(0);
	    		area.mediane.ajoute(fare + tip);
	    		area.mediane.getMediane();
				area.calculprofitability();
				listArea.add(area);
				}
	    	}
	    
	    // Créer une nouvelle liste pour sauvegarder tous les records dans 30mins.  
	    listDropoff.add(record);
	    for(int i=0;i<listDropoff.size();i++){
	    	if ((tempsDroppffRecord- listDropoff.get(i).getDropoff_datetime()) >(2*duree15min)){
	    		listDropoff.remove(i);
	    		}
	    	}
	    
	    /**
	     * D'abord, on crée une liste pour sauvegarder tous les licences des taxis que on 
	     * peut trouver dans les records de 'listDropoff'.
	     * C.a.d., ces sont tous les licences des taxis dans la durée de 30mins aux tous 
	     * les areas.
	     * Ensuite, on choisit une licence et un area pour calculer le fois qu'il apparaît 
	     * dans la durée de 30mins.  
	     * Après, on le calcule pour tous les areas et les licences.
	     * Enfin, on ne choisi que les voitures qui apparaît une fois dans la durée de 30mins.
	     * (= les taxis vides)
	     */
	    Set<String> listLicence =new  HashSet<String>();
	    listLicence.clear();
	    
	    for(int i=0;i<listDropoff.size();i++){
	    	listLicence.add(listDropoff.get(i).getHack_license());
	    	}

	    for(Areas area : listArea){
	    	for(String string:listLicence){
	    		int time=0;
	    		for(int i=0;i<listDropoff.size();i++){
	    			if (string.equals(listDropoff.get(i).getHack_license())
	    					&&(area.getunit().equals(convertTounit(listDropoff.get(i).getPickup_latitude(),listDropoff.get(i).getPickup_longitude())))){
	    				time++;
	    				}
	    			}
	    		if(time==1){
	    			area.setNbTaxisEmpty(area.getNbTaxisEmpty()+1);
	    			}																	
	    		}
	    	}
	    
	    for(int i = 0;i<listArea.size();i++){
	    	listArea.get(i).calculprofitability();
	    	}	
	    
	    // Finir.
	    tempsFini = System.nanoTime();
	    delay = tempsFini - tempsCommence;
	    
	    // output the result.
	    for(int i=0;i<listArea.size();i++){
/*	    	System.out.println("[unit : " + listArea.get(i).getunit() + "]" +
							   " [Taxis Vides : " + listArea.get(i).getNbTaxisEmpty() + "]" + 
							   " [Median profit : " + listArea.get(i).mediane.getMediane() + "]" + 
							   " [Profitability : "  + listArea.get(i).getProfitability() + "]" +
							   " [Delay : " + delay + "]");
*/							   
	    	this.writeLine("[unit : " + listArea.get(i).getunit() + "]" +
							   " [Taxis Vides : " + listArea.get(i).getNbTaxisEmpty() + "]" + 
							   " [Median profit : " + listArea.get(i).mediane.getMediane() + "]" + 
							   " [Profitability : "  + listArea.get(i).getProfitability() + "]" +
							   " [Delay : " + delay + "]");
	    	}
	    }

	// Supprimer les cases qui sont hors 15min. 
	private void supprimerRecord(long temps)
	{
		while (true)
		{
			DebsRecord record = list15.getFirst();
			if (record.getDropoff_datetime() < temps)
			{
				String unit = convertTounit(record.getPickup_latitude(), record.getPickup_longitude());
				listAreaProfitable.get(unit).supprime(record.getFare_amount() + record.getTip_amount());
				list15.removeFirst();
			}
			else 
			{
				return;
			}
		}
	}
}
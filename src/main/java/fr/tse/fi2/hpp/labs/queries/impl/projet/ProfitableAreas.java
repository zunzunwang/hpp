package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.text.TabableView;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class ProfitableAreas extends AbstractQueryProcessor {

	long currentTime ;
	final long MINUTES_FENETRES = 15 * 60 * 1000;
	LinkedList<DebsRecord> area15min ;
	LinkedList<DebsRecord> fenetre15min;
	LinkedList<DebsRecord> fenetreDrop30min;
	LinkedList<DebsRecord> fenetrePick30min;
	HashMap<String, ListMedian> profitArea;
	ArrayList<Areas> array10most;
	public ProfitableAreas(QueryProcessorMeasure measure) {
		super(measure);
		currentTime = 0;
		area15min= new LinkedList<DebsRecord>();
		fenetre15min = new LinkedList<DebsRecord>();
		array10most = new ArrayList<Areas>();
		profitArea = new HashMap<String, ListMedian>();
		fenetreDrop30min = new LinkedList<DebsRecord>();
		fenetrePick30min = new LinkedList<DebsRecord>();
	}

	@Override
	protected void process(DebsRecord record) {
		
		currentTime=record.getDropoff_datetime();
		

		
		// informations concernant le trajet
				String caseDepart  = convertTounit(record.getPickup_latitude(), record.getPickup_longitude());
				float fare = record.getFare_amount();
				float tip  = record.getTip_amount();
				

				
				// Ajout ou mise à jour du profit de la case dans la hashmap
				// on ne doit prendre en compte que les trajets sans erreurs 
				// (donc fare et tip doivent etre positifs)
				if (fare >= 0 && tip >= 0)
				{
					// Ajout de la course dans la fenetre des 15 min
					fenetre15min.add(record);
					
					if (profitArea.containsKey(caseDepart))
					{
						profitArea.get(caseDepart).ajoute(fare + tip);
						
					}
					
					else
					{
						ListMedian tmp = new ListMedian();
						tmp.ajoute(fare + tip);
						profitArea.put(caseDepart, tmp);
					}
				}
				
				
				// On supprime les gains des courses qui datent de plus de 15 min
				removeEarnings15min(currentTime - MINUTES_FENETRES);
				
				for (String unit : profitArea.keySet())
				{
					System.out.println(profitArea.get(unit).medianFareAndTip.size());
					String profit = String.valueOf(profitArea.get(unit).getMediane());
					System.out.println("Area : " + unit + "\tProfit : " + profit);
				}
				
				System.out.println("-------------");
				
			

		//Récupération des zones dans les 15 dernière minutes et calcul du nombre de trajet
				ArrayList<Areas> tabArea = new ArrayList<Areas>();
				
				area15min.add(record);
				for(int i=0;i<area15min.size();i++){
					if ((currentTime- area15min.get(i).getPickup_datetime()) >MINUTES_FENETRES){
						area15min.remove(i);
					}
				}
				
				for(int i=0;i<area15min.size();i++){
					Areas a = new Areas();
					a.setunit(convertTounit(area15min.get(i).getPickup_latitude() , area15min.get(i).getPickup_longitude()));
					boolean find = false;
					for(int j=0;j<tabArea.size();j++){
						if(a.getunit().equals(tabArea.get(j).getunit())){
							tabArea.get(j).mediane.ajoute(fare + tip);
							tabArea.get(j).mediane.getMediane();
							tabArea.get(j).calculprofitability();
							find = true;
						}
					}
					if(!find){
						a.setNbTaxisEmpty(0);
						a.mediane.ajoute(fare + tip);
						a.mediane.getMediane();
						a.calculprofitability();
						tabArea.add(a);
						
					}
					
					
				}
			
		//Calcul du nombre de taxi vide				
				fenetreDrop30min.add(record);
				for(int i=0;i<fenetreDrop30min.size();i++){
					if ((currentTime- fenetreDrop30min.get(i).getDropoff_datetime()) >(2*MINUTES_FENETRES)){
						fenetreDrop30min.remove(i);
						
					}
				}

				Set<String> listLicence =new  HashSet<String>();
				listLicence.clear();
				for(int i=0;i<fenetreDrop30min.size();i++){
					listLicence.add(fenetreDrop30min.get(i).getHack_license());					
				}
				/***vérifier est ce qu'il y a les même élémens dans la liste ***/
/*				for(String string:listLicence){
					System.out.println(string);
				}
*/				
				
				for(Areas area : tabArea){//pour la même place
					for(String string:listLicence){
						int time=0;
						for(int i=0;i<fenetreDrop30min.size();i++){//对于同一辆车求出现的次数
							if (string.equals(fenetreDrop30min.get(i).getHack_license())
									&&(area.getunit().equals(convertTounit(fenetreDrop30min.get(i).getPickup_latitude(),fenetreDrop30min.get(i).getPickup_longitude())))){
							time++;														
							}
						}
						if(time==1){
							area.setNbTaxisEmpty(area.getNbTaxisEmpty()+1);

						}																	
					}
				}			
				for(int i = 0;i<tabArea.size();i++){
					tabArea.get(i).calculprofitability();
				}
				
				
				System.out.println(fenetrePick30min.size());
				for(int i=0;i<tabArea.size();i++){
					System.out.println("cell : " + tabArea.get(i).getunit() + 
										" Taxi Empty : " + tabArea.get(i).getNbTaxisEmpty() + " Median profit : " + tabArea.get(i).mediane.getMediane() + " Profitability : "  + tabArea.get(i).getProfitability());
				}
		
	}

	/**
	 * Retourne la case a laquelle appartient la coordonnee (latitude,longitude).
	 * @param latitude
	 * @param longitude
	 * @return La case sous la forme d'un entier. Les coordonnees de la case sont comprises entre
	 * 1 et 600. On retourne (unitX * 1000 + unitY)
	 */
	private String convertTounit(float latitude, float longitude)
	{
		// latitude = ordonnee; longitude = abscisse
		
		// longueur et largeur de chaque case
		double step_abscisse = 0.005986/2;
		double step_ordonnee = 0.004491556/2;
		
		// origine de la grille
		double starting_abscisse = -74.913585;
		double starting_ordonnee = 41.474937;
		
		// nombre de cases separant le point en parametre de l'origine de la grille
		double unitX = (longitude - starting_abscisse) / step_abscisse;
		double unitY = (starting_ordonnee - latitude ) / step_ordonnee;
		
		// on ne garde que la partie entiere du nombre de cases
		Integer X = (int) (Math.round(unitX) + 1);
		Integer Y = (int) (Math.round(unitY) + 1);
		String result = X.toString() + "." + Y.toString();
		return result;
	}
	
	private void removeEarnings15min(long time)
	{
		while (true)
		{
			DebsRecord elem = fenetre15min.getFirst();
			
			// on enleve le gain des courses qui sont en dehors de la fenetre de 15 min
			// et on enleve ces debsRecord de la fenetre
			if (elem.getDropoff_datetime() < time)
			{
				// trouve la case de depart de cette course
				String unit = convertTounit(elem.getPickup_latitude(), elem.getPickup_longitude());
				
				// on est sur que le gain peut etre enleve car si le debsRecord est dans la 
				// fenetre, alors on a ajoute son gain
				profitArea.get(unit).supprime(elem.getFare_amount() + elem.getTip_amount());
				
				// supprime de la fenetre
				fenetre15min.removeFirst();
			}
			else 
			{
				return;
			}
		}
	}
}
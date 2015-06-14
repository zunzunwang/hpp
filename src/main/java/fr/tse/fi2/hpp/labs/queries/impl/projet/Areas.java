
package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;

public class Areas {
    
    private int nbTaxisEmpty;
    private ArrayList<DebsRecord> record;
    private float profitability;    
    private String unit;
    protected ListMedian mediane;

    public Areas() {
        setNbTaxisEmpty(0);
        setRec(new ArrayList<DebsRecord>());
        mediane = new ListMedian();
        
    }
    
    // Ajouter un revenu.
    public void addIncome(DebsRecord recordTest) {
        if(recordTest.getFare_amount() + recordTest.getTip_amount() > 0) {
            record.add(recordTest);
        }
    }
    
    // Set le record.
    public void setRec(ArrayList<DebsRecord> rec) {
        this.record = rec;
    }

    // @return le record.
    public ArrayList<DebsRecord> getRec() {
        return record;
    }
    
    // Set la normalisation d'une place. 
    public void setunit(String unit) {
        this.unit = unit;
    }
    
    // @return la normalization d'une place.
    public String getunit() {
        return unit;
    }
    
    // Set le nombre des taxis vides.
    public void setNbTaxisEmpty(int nbTaxisVides) {
        this.nbTaxisEmpty = nbTaxisVides;
    }
        
    // @return la somme des taxis vides.
     public int getNbTaxisEmpty() {
         return nbTaxisEmpty;
    }
    
    /**
     * The profitability of an area is determined by dividing the area profit 
     * by the number of empty taxis in that area within the last 15 minutes.
     */
    public void calculprofitability(){
        if(nbTaxisEmpty>0){
            profitability = mediane.getMediane()/nbTaxisEmpty;
        }
        else{
            profitability=0;
        }
    }
    
    // Set la rentabilité.
    public void setProfitability(float profitability) {
        this.profitability = profitability;
    }

    // @return la rentabilité.
    public float getProfitability() {
        return profitability;
    }

    // Récrire les fonctions des hashCode() and equals()
    @Override
    public int hashCode() {
        final int prime = 233;
        int result = 1;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + ((mediane == null) ? 0 : mediane.hashCode());
        result = prime * result + nbTaxisEmpty;
        result = prime * result + Float.floatToIntBits(profitability);
        result = prime * result + ((record == null) ? 0 : record.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() == obj.getClass()){
            Areas areas = (Areas) obj;
            if (unit == null) {
                if (areas.unit != null)
                    return false;
            } else if(unit.equals(areas.unit)){
                if (mediane == null) {
                    if (areas.mediane != null)
                        return false;
                } else if (mediane.equals(areas.mediane)){
                    if (nbTaxisEmpty == areas.nbTaxisEmpty){
                        if (profitability == (areas.profitability)){
                            if (record == null) {
                                if (areas.record != null)
                                    return false;
                            } else if (record.equals(areas.record)){                          	
                                return true;
                            }
                        }return false;                    	
                    }return false;                	                	
                }return false;            	            	            	
            } return false;
        }return false;
     }

    
}
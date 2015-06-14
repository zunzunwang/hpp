package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.Collections;

public class ListMedian 
{
    ArrayList<Float> medianFareAndTip;

    // Constructeur.
    public ListMedian() 
    {
        medianFareAndTip = new ArrayList<Float>();
    }
    
    // Ajouter un nouveau revenu (Fare+Tip) dans la liste medianFareAndTip.
    public void ajoute(float income)
    {
        medianFareAndTip.add(income);
    }
    
    // Supprimer un revenu déjà existé de la liste medianFareAndTip.
    public void supprime(float income)
    {
        medianFareAndTip.remove(income);
    }
    
    // @return le valeur de median de la liste medianFareAndTip.
    public float getMediane() {
        // TODO Auto-generated method stub
        if (medianFareAndTip.size() == 0)
        {
            return 0;    
        }
        
        Collections.sort(medianFareAndTip);
    
        int mid = medianFareAndTip.size() / 2;
        if (medianFareAndTip.size() % 2 == 0)
        {
            return (medianFareAndTip.get(mid) + medianFareAndTip.get(mid-1)) / 2;
        }
        else
        {
            return medianFareAndTip.get(mid);
        }
    }
}

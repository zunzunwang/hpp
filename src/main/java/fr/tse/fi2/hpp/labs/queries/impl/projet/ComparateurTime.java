package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.List;

import fr.tse.fi2.hpp.labs.beans.NewRecord;

public class ComparateurTime {
	public ComparateurTime(){
		
	}
	public List<NewRecord> freshTime(List<NewRecord> list){		
        int length=list.size();
		int j;				 
		int i;				 
		NewRecord key;			 
		for(j=1; j<length; j++){
//			System.out.println("j:"+j);
//			System.out.println("size:"+list.size());

			key=list.get(j);
			i=j-1;
			while(i>=0 && list.get(i).getTimes() < key.getTimes()){
				list.set(i+1, list.get(i)); 
				i -= 1;   		
			}
			list.set(i+1, key);  
	}
		return list;

}

}
package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.List;

import fr.tse.fi2.hpp.labs.beans.NewRecord;

public class ComparateurList {
	public ComparateurList(){
		
	}
	public List<NewRecord> freshList(List<NewRecord> list, int n, int nAll){

		int j;				 
		int i;				 
		NewRecord key;			 
		for(j=(nAll-n+1); j<nAll; j++){
//			System.out.println("j:"+j);
//			System.out.println("size:"+list.size());

			key=list.get(j);
			i=j-1;
			while(i>=0 && list.get(i).getDropoff_datetime() < key.getDropoff_datetime()){
				list.set(i+1, list.get(i)); 
				i -= 1;   		
			}
			list.set(i+1, key);  
	}
		return list;

}
/*	
	public static void main(String[] args){
		List<NewRecord> dad = new ArrayList<NewRecord>() ;
		dad.add(new NewRecord(1,2,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(1,2,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(2,3,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(2,3,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(2,3,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(3,4,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(4,5,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(5,6,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(4,5,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(6,7,new GridPoint(2, 2), new GridPoint(2, 2),100));
		dad.add(new NewRecord(5,6,new GridPoint(2, 2), new GridPoint(2, 2),100));
		List<NewRecord> dad2 = null ;
		dad2 = ComparateurList.freshList(dad, 11, 11);
		for(NewRecord aaa: dad2){
			System.out.println(aaa.getDropoff_datetime());
		}
		}
*/
}
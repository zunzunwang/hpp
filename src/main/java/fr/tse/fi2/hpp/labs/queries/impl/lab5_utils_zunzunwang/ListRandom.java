package fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang;

import java.util.Random;

public class ListRandom {
	int[] array;
	public ListRandom(int n){
	Random random = new Random();
	array= new int[n];
	for (int i = 0; i < n; i ++) {
		array[i] = random.nextInt(10000000);
		}		
/*		  
		  for (int i = 0; i < n; i ++) {
		   System.out.println(array[i]);
		  }
*/
	}
	
	public int[] getListrandom(){
		return array;
		}
}

package fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang;

public class Merge_InsertSort {
	/**
	 * 
	 * @param a要进行排序的原始列表
	 * @param s起始排序位置
	 * @param len排序长度此处应该为20我们假设20一下的都使用insertion来计算
	 */
	Merge_InsertSort(int[]a,int s,int len){
		if(a.length>20){
			MergeSort.MergeSort(a, 0, 2 * len );
			
			
		}else{
			InsertSort.InsertSort(a);
		}
		
	}

}

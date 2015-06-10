package fr.tse.fi2.hpp.labs.test;

import java.util.Arrays;
import org.junit.Test;

import fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang.InsertSort;
import fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang.ListRandom;
import fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang.MergeSort;

public class TestSorting {
	ListRandom listrandom = new ListRandom(100000);
	int[] testlist=listrandom.getListrandom();
	
	
	

	@Test
	public void testMergeSort() {
		long timestart = System.nanoTime();
		MergeSort.MergeSort(testlist, 0, 1);
		long timefin =System.nanoTime();
		System.out.println("test_merge"+(timefin-timestart));
		}
	
	@Test
	public void InsertSort() {
		long timestart = System.nanoTime();
		InsertSort.InsertSort(testlist);
		long timefin =System.nanoTime();
		System.out.println("test_insert"+(timefin-timestart));
		}
	


	@Test
	public void testsort() {
		long timestart = System.nanoTime();
		Arrays.sort(testlist);
		long timefin =System.nanoTime();
		System.out.println("test_arraysort"+(timefin-timestart));
	}
	
	
	

}

package fr.tse.fi2.hpp.labs.queries.impl.lab5_utils_zunzunwang;

import static org.junit.Assert.*;

import org.junit.Test;

public class MergeSort {
	/**
	 * <pre>
	 * 二路归并
	 * 原理：将两个有序表合并和一个有序表
	 * </pre>
	 * 
	 * @param a
	 * tableau qu'on utilise
	 * @param s
	 * 第一个有序表的起始下标
	 * @param m
	 * 第二个有序表的起始下标
	 * @param t
	 * 第二个有序表的结束小标
	 * @param len
	 * 每次归并的有序集合的长度
	 * 
	 */
	public static void MergeSort(int[]a,int s,int len){
		int size = a.length;//得到整个数组的长度
		int mid = size / (len << 1);//即设置的
//		System.out.println("mid="+mid);
		int c = size & ((len << 1) - 1);
//		System.out.println("c="+c);

		// -------归并到只剩一个有序集合的时候结束算法-------//
		if (mid == 0)
		return;
		// ------进行一趟归并排序-------//
		for (int i = 0; i < mid; ++i) {//i从0开始计数 对二分后的第一组数据进行排序
		s = i * 2 * len;//s为第一个有序表的起始下标。进入时i的值为0。
//		System.out.println("merge("+a+","+ s+","+( s + len) +","+ ((len << 1) + s - 1)+")");
		merge(a, s, s + len, (len << 1) + s - 1);//merge(a,s,m,t)
		}
		// -------将剩下的数和倒数一个有序集合归并-------//
		if (c != 0)
		merge(a, size - c - 2 * len, size - c, size - 1);
		// -------递归执行下一趟归并排序------//
		MergeSort(a, 0, 2 * len );
		}

	/**
	 * 
	 * @param a
	 * 为要比较的数组
	 * @param s
	 * 第一个有序表的起始下标
	 * @param m
	 * 为第二有序表的起始下标
	 * @param len
	 * 每次归并的有序集合的长度
	 * @param t
	 * 第二个有序表的结束下标
	 */
	private static void merge(int[] a, int s, int m, int t) {
	//-------建立第一个临时数组用于储存有序的表-------//	
	int[] tmp = new int[t - s + 1];//t－s＋1计算出了要排序的数组的整个长度
	int i = s, j = m, k = 0;
	while (i < m && j <= t) {
	if (a[i] <= a[j]) {
	tmp[k] = a[i];
	k++;
	i++;
	} else {
	tmp[k] = a[j];
	j++;
	k++;
	}
	}
	while (i < m) {
	tmp[k] = a[i];
	i++;
	k++;
	}
	 
	while (j <= t) {
	tmp[k] = a[j];
	j++;
	k++;
	}
	System.arraycopy(tmp, 0, a, s, tmp.length);
}



	
	
	
/*	

	@Test
	public void test() {
		int[] a = new int[] { 4, 3, 6, 1, 2, 15,10  };
		MergeSort(a, 0, 1);
		for (int i = 0; i < a.length; ++i) {
		System.out.print(a[i] + " ");
		}
		}
*/
	}



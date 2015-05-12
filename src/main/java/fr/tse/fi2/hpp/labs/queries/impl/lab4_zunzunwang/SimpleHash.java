package fr.tse.fi2.hpp.labs.queries.impl.lab4_zunzunwang;

public class SimpleHash {
	  /* 哈希函数类 */

	     //DEFAULT_SIZE的值，即用于结果的最大的字符串长度。
	     //seed为计算hash值的一个给定key，具体对应上面定义的seeds数组
	     private int defaultSize;
	     private int seed;

	     public SimpleHash(int defaultSize, int seed) {
	         this.defaultSize = defaultSize;
	         this.seed = seed;
	     }

	
		//计算hash值的具体算法,hash函数，采用简单的加权和hash
	     public int hash(String value) {
	         //int的范围最大是2的31次方减1，或超过值则用负数来表示
	         int result = 0;
	         int len = value.length();
	         for (int i = 0; i < len; i++) {
	            //数字和字符串相加，字符串转换成为ASCII码
	            result = seed * result + value.charAt(i);
	            //System.out.println(result+"--"+seed+"*"+result+"+"+value.charAt(i));
	         }
	     //  System.out.println("result="+result+";"+((cap - 1) & result));
	     //  System.out.println(414356308*61+'h');  执行此运算结果为负数，为什么？
	         //&是java中的位逻辑运算，用于过滤负数（负数与进算转换成反码进行）。
	         return (defaultSize - 1) & result;
	     }
}


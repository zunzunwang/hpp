package fr.tse.fi2.hpp.labs.queries.impl.lab4_zunzunwang;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;


import java.util.ArrayList;
import java.util.BitSet;
;
//传统的Bloom filter 不支持从集合中删除成员。
//Counting Bloom filter由于采用了计数，因此支持remove操作。
//基于BitSet来实现，性能上可能存在问题
public class SimpleBloomFilter extends AbstractQueryProcessor{

  //DEFAULT_SIZE为2的25次方
  private static final int DEFAULT_SIZE = 2 << 24;//初始化了m的大小
  /* 不同哈希函数的种子，一般应取质数,seeds数据共有7个值，则代表采用7种不同的HASH算法 */
  private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };
  //BitSet实际是由“二进制位”构成的一个Vector。假如希望高效率地保存大量“开－关”信息，就应使用BitSet.
  //BitSet的最小长度是一个长整数（Long）的长度：64位
  private static BitSet bits = new BitSet(DEFAULT_SIZE);//建立了一个bitset组为了存储
  /* 哈希函数对象 */
  private static SimpleHash[] func = new SimpleHash[seeds.length];//创建哈希函数
  private static ArrayList<DebsRecord> ListeRoute = new ArrayList<DebsRecord>();
  private static DebsRecord recordTest;
  private int compte=0;
  
  
  public SimpleBloomFilter(QueryProcessorMeasure measure) {

		super(measure);
		// TODO Auto-generated constructor stub
		  for (int i = 0; i < seeds.length; i++) {
		         //给出所有的hash值，共计seeds.length个hash值。共7位。
		         //通过调用SimpleHash.hash(),可以得到根据7种hash函数计算得出的hash值。
		         //传入DEFAULT_SIZE(最终字符串的长度），seeds[i](一个指定的质数)即可得到需要的那个hash值的位置。
		         func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);//建立了7个hash函数。
		         }		
	}
	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub	
			ListeRoute.add(record);
			String recordstring =null;
			recordstring += record.getPickup_longitude();
			recordstring += record.getPickup_latitude();
			recordstring += record.getDropoff_longitude();
			recordstring += record.getDropoff_latitude();
			recordstring += record.getHack_license();
			System.out.println(recordstring);
			this.add(recordstring);
			compte++;
			if(compte==20){
				recordTest = record;
			}					
		}
/*	public void recordprocess(){
		for(DebsRecord i : ListeRoute ){
			String recordstring =null;
			recordstring += i.getPickup_longitude();
			recordstring += i.getPickup_latitude();
			recordstring += i.getDropoff_longitude();
			recordstring += i.getDropoff_latitude();
			recordstring += i.getHack_license();
			System.out.println(recordstring);
			this.add(recordstring);
			
		}
		
		
	}
	*/
	
	public static String getRecord(){
		String recordstring=null;
		recordstring += recordTest.getPickup_longitude();
		recordstring += recordTest.getPickup_latitude();
		recordstring += recordTest.getDropoff_longitude();
		recordstring += recordTest.getDropoff_latitude();
		recordstring += recordTest.getHack_license();
		return recordstring;
	}
 /* 
  public static void main(String[] args) {
     String value = "false";
     //定义一个filter，定义的时候会调用构造函数，即初始化七个hash函数对象所需要的信息。
     SimpleBloomFilter filter = new SimpleBloomFilter();
     //判断是否包含在里面。因为没有调用add方法，所以肯定是返回false
     System.out.println(filter.contains(value));
     filter.add(value);
     System.out.println(filter.contains(value));
  }
  
 
  //构造函数
  public SimpleBloomFilter() {
     for (int i = 0; i < seeds.length; i++) {
         //给出所有的hash值，共计seeds.length个hash值。共7位。
         //通过调用SimpleHash.hash(),可以得到根据7种hash函数计算得出的hash值。
         //传入DEFAULT_SIZE(最终字符串的长度），seeds[i](一个指定的质数)即可得到需要的那个hash值的位置。
         func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
     }
  }
*/
	
	
	
  // 将字符串标记到bits中，即设置字符串的7个hash值函数为1
  public void add(String value) {
     for (SimpleHash f : func) {
         bits.set(f.hash(value), true);
     }
  }

  //判断字符串是否已经被bits标记
  public static boolean contains(String value) {
     //确保传入的不是空值
     if (value == null) {
         return false;
     }
     boolean ret = true;
     //计算7种hash算法下各自对应的hash值，并判断
     for (SimpleHash f : func) {
         //&&是boolen运算符，只要有一个为0，则为0。即需要所有的位都为1，才代表包含在里面。
         //f.hash(value)返回hash对应的位数值
         //bits.get函数返回bitset中对应position的值。即返回hash值是否为0或1。
         ret = ret && bits.get(f.hash(value));
     }
     return ret;
  }
}
  

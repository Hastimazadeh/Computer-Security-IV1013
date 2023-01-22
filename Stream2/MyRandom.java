//THIS IS TASK 2 FOR StreamCipher ASSIGNMENT

import java.util.Random;

public class MyRandom extends Random {

	//numbers from:
	//https://en.wikipedia.org/wiki/Linear_congruential_generator
	public long seed;
	public long a = 48271;// old tries: 1103515245L;
	public long c = 0;//old tries: 12345;
	public long m = 2147483647;//old tries: 1<<31; //public long m = (long) Math.pow(2, 48);

	//Constructors:
	MyRandom(){
		
	}
	
	MyRandom(long seeed){
		this.setSeed(seeed);
	}
	
	//Methods:
	@Override 
	public int next(int bits) { //bits=length
		if (bits <= 0 || bits > 32 ) {
			System.out.println("bits should be in the interval of [1,32] ");
			System.exit(2);
		}
		seed =  (seed * a + c) % m  ;
		int result = (int) (seed & ((1L << bits) - 1));

		return result;
	}
	@Override 
	public void setSeed(long seeeed) {
		this.seed = seeeed;
	}
	

}

//Task 3 after failing check 5, made changes

import java.util.Random;

public class MyRandom extends Random {

	public byte[] key;
	byte[] S = new byte[256]; //First, the array "S" is initialised to the identity permutation
	int i = 0;
	int j = 0;

	//Constructors:
	MyRandom(){
	}
	
	MyRandom(byte[] key){
		this.key = key;
	
		//This part of the code is inspired by lecture slides (lecture 2 slide 45)
		for (int i = 0; i < 256; i++) {
			S[i] = (byte) i;
		}
		int j = 0;
		for (int i = 0; i < 256; i++) {
			j = Math.floorMod(j + S[i] + key[i % key.length], 256);
			swapValues(i , j);
		}
	}
	
	//Methods:
	@Override 
	public int next(int bits) { 
		
		//This part of the code is inspired by lecture slides (lecture 2 slide 46)
		i =  Math.floorMod((i + 1) , 256);
		j =  Math.floorMod((j + S[i]) , 256);
		swapValues(i, j);

		int result = S[Math.floorMod((S[i] + S[j]) , 256)] & ((1 << bits) - 1);
		//System.out.println(result);
		return result;
	}
	
	@Override 
	public void setSeed(long seeeed) {
	}
	
	private void swapValues( int i,  int j) {
		byte temp = S[i];
		S[i] = S[j];
		S[j] = temp;
	}
	

}

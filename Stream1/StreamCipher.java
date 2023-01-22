//THIS IS TASK 1 FOR StreamCipher ASSIGNMENT
// new changes: I changed inputtext from byte to int

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class StreamCipher {

	public static void encryptdecrypt(long key, String inputfile, String outputfile) {
		
		// Input file
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(inputfile);
		} catch (FileNotFoundException e) {
			System.out.println("Error: The input file is invalid.");
			System.exit(1);
		}
		BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);

		
		//Output file
		FileOutputStream fileOutput = null;
		try {
			fileOutput = new FileOutputStream(outputfile);
		} catch (FileNotFoundException e) {
			System.out.println("Error: The output file is invalid.");
			System.exit(2);
		}
		BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
		
		//key
		Random rnd = new Random(key);
		
		
		int inputText;
			try {
				while ( (inputText =  bufferedInput.read()) != -1) {
					int outputText = (inputText^rnd.nextInt(256));
					bufferedOutput.write(outputText);
				}
			} catch (IOException e1) {
				System.out.println("Error: Could not read/write to the file.");
				System.exit(3);
			}
	

		try {
			bufferedInput.close();
		} catch (IOException e) {
			System.out.println("Error: Closing the input file failed.");
			System.exit(4);
			
		}
		try {
			bufferedOutput.close();
		} catch (IOException e) {
			System.out.println("Error: Closing the output file failed.");
			System.exit(5);
		}
		
	}
	
	public static void main(String[] args) {
		if ( args.length != 3 ) {
			System.out.println("Error: There should be 3 arguments in the following order: <key> <infile> <outfile>");
			System.exit(6);
		}
		
		try {
			long key = Long.parseLong(args[0]);
			String infile = args[1];
			String outfile = args[2];
			encryptdecrypt(key, infile, outfile);
		} catch (NumberFormatException e) {
			System.out.println("Error: Key should be a number.");
			System.out.println("Make sure that the 3 arguments are in the following order: <key> <infile> <outfile> ");
			System.exit(7);
		}

	}

}

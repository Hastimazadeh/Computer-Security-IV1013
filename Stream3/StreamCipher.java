//Task 3 after failing check 5, made changes

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;

public class StreamCipher {

	public static void encryptdecrypt(byte[] key, String inputfile, String outputfile) {

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
		MyRandom rnd = new MyRandom(key);		

		int inputText;
		try {
			while ( (inputText = bufferedInput.read()) != -1) {
				int outputText = (inputText^rnd.next(8));
				//System.out.printf("%02x ", outputText);
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
		
		BigInteger bigInt = new BigInteger(args[0]);
		byte[] key = bigInt.toByteArray();
		//before the changes for check 5:
			//args[0].getBytes(Charset.defaultCharset());
		String infile = args[1];
		String outfile = args[2];
		encryptdecrypt(key, infile, outfile);


	}
	
}

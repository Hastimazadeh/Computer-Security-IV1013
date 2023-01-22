import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PasswordCrack {

	//function for reading the input file
	public static String[] readInputFile(String file) {
		List<String> list = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				list.add(line); //goes through line and adds it to the list until null
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Error: Failed to read the file. Make sure the file exists and is readable.");
			System.exit(1);
		}
		return list.toArray(String[]::new); //return the list in the form of an array of string  
	}

	public static String getPassHash(String line) {
		return line.split(":")[1]; //takes the string, splits, takes the second element ([1]) which is hash
	}

	public static String getUserName(String line) {
		return line.split(":")[0]; //takes the string, splits, takes the first element ([0]) which is user name
	}

	public static List<String> getUsefulNames(String line) { //to get the first name, last name, middle name etc
		String[] allNames = line.split(":")[4].split(" "); //the 5th section in the line is where it contains names, divided by space
		List<String> usefulNames = new ArrayList<>(); //to store the names that are not initials (contain ".")
		for (String name : allNames) {
			if (!name.contains(".")) {
				usefulNames.add(name);
			}
		}
		return usefulNames;
	}

	public static void crackInThreads(String[] dict) {
		int numOfThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService dictPool = Executors.newFixedThreadPool(numOfThreads);
		int size = dict.length;
		int subSize = size / numOfThreads;
		for (int i = 0; i < numOfThreads; i++) {// to create the threads
			//to take care of rounding the numbers for subSize (if they all don't have equal numbers)
			int max = Math.min(size, (i + 1) * subSize);
			int min = i * subSize;
			String[] subList = Arrays.copyOfRange(dict, min, max); //creating the sub lists
			CrackThread attacker = new CrackThread(subList);// use this list of dictionary words to crack
			dictPool.submit(attacker); //take the given thread and call run()
		}
		try {
			dictPool.shutdown(); // initiates the shutdown
			// It will wait until either it is done or it takes 1 day
			dictPool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			System.out.println("System error. Thread prematurely interrupted");
			System.exit(1);
		}

	}

	static CopyOnWriteArrayList<String> passHash;

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Error: There should be 2 arguments in the following order: <dictionary> <passwd>");
			System.exit(1);
		}
		String[] dictionary = readInputFile(args[0]);
		String[] passwdFile = readInputFile(args[1]);
		List<String> names = new ArrayList<>();

		passHash = new CopyOnWriteArrayList<>();
		for (String h : passwdFile) {
			passHash.add(getPassHash(h));
			names.addAll(getUsefulNames(h));
			//names.add(getUserName(h));
		}
		crackInThreads(names.toArray(String[]::new));
		crackInThreads(dictionary);
	}

}

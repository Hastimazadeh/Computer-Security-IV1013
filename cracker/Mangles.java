import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mangles {
	//all the mangles mentioned on canvas page:

	public static String prepend(String dicWord, char c) {
		return c + dicWord;
	}

	public static String append(String dicWord, char c) {
		return dicWord + c;
	}

	public static String deleteFirstChar(String dicWord) {
		StringBuilder sb = new StringBuilder(dicWord);
		sb.deleteCharAt(0);
		return sb.toString();
	}

	public static String deleteLastChar(String dicWord) {
		StringBuilder sb = new StringBuilder(dicWord);
		sb.deleteCharAt(dicWord.length() - 1);
		return sb.toString();
	}

	public static String reverse(String dicWord) {
		return new StringBuilder(dicWord).reverse().toString();
	}

	public static String duplicate(String dicWord) {
		return dicWord + dicWord;
	}

	public static String reflect1(String dicWord) {
		return dicWord + reverse(dicWord);
	}

	public static String reflect2(String dicWord) {
		return reverse(dicWord) + dicWord;
	}

	public static String uppercase(String dicWord) {
		return dicWord.toUpperCase();
	}

	public static String lowercase(String dicWord) {
		return dicWord.toLowerCase();
	}

	public static String capitalize(String dicWord) {
		return Character.toUpperCase(dicWord.charAt(0)) + dicWord.substring(1);
	}

	public static String ncapitalize(String dicWord) {
		return Character.toLowerCase(dicWord.charAt(0)) + dicWord.substring(1);
	}

	public static String toggle1(String dicWord) {
		char[] chars = dicWord.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i % 2 == 0) {
				chars[i] = Character.toLowerCase(chars[i]);
			} else {
				chars[i] = Character.toUpperCase(chars[i]);
			}
		}
		return new String(chars);
	}

	public static String toggle2(String dicWord) {
		char[] chars = dicWord.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i % 2 != 0) {
				chars[i] = Character.toLowerCase(chars[i]);
			} else {
				chars[i] = Character.toUpperCase(chars[i]);
			}
		}
		return new String(chars);
	}

	// a function that calls all the mangles so we don't have to call one by one and puts all the alternations of the word in a list
	public static String[] callAllFunc(String dictWord) {
		List<String> listAllFunc = new ArrayList<>(); //the list that has all the alternations of the word

		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		char[] lowerCaseAlphabet = alphabet.toCharArray();
		char[] upperCaseAlphabet = alphabet.toUpperCase().toCharArray();

		if (dictWord.length() < 8) { //the mangles are only useful for words that are less than 8 chars

			listAllFunc.add(deleteLastChar(dictWord));
			listAllFunc.add(duplicate(dictWord));
			listAllFunc.add(reflect1(dictWord));
			listAllFunc.add(reflect2(dictWord));
			for (int i = 0; i < 10; i++) {
				listAllFunc.add(append(dictWord, (char) i));
			}
			for (int i = 0; i < 26; i++) {
				listAllFunc.add(append(dictWord, upperCaseAlphabet[i]));
				listAllFunc.add(append(dictWord, lowerCaseAlphabet[i]));
			}
		}

		//these mangles are useful to all the words regardless of their length 
		listAllFunc.add(deleteFirstChar(dictWord));
		listAllFunc.add(reverse(dictWord));
		listAllFunc.add(uppercase(dictWord));
		listAllFunc.add(lowercase(dictWord));
		listAllFunc.add(capitalize(dictWord));
		listAllFunc.add(ncapitalize(dictWord));
		listAllFunc.add(toggle1(dictWord));
		listAllFunc.add(toggle2(dictWord));
		for (int i = 0; i < 10; i++) {
			listAllFunc.add(prepend(dictWord, (char) i));
		}
		for (int i = 0; i < 26; i++) {
			listAllFunc.add(prepend(dictWord, upperCaseAlphabet[i]));
			listAllFunc.add(prepend(dictWord, lowerCaseAlphabet[i]));
		}

		listAllFunc.remove(dictWord);

		return listAllFunc.stream().distinct().collect(Collectors.toList()).toArray(String[]::new);
		//return listAllFunc.toArray(String[]::new);
	}

}

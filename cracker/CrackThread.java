public class CrackThread implements Runnable {

	public String[] dictionary;

	public CrackThread(String[] dict) {
		this.dictionary = dict;
	}

	@Override
	public void run() {
		crack();
	}

	public void crack() {
		//no mangles applied 
		for (String word : dictionary) {
			for (String hash : PasswordCrack.passHash) {
				comparison(word, hash);
			}
		}
		//one mangle applied to words 
		for (String word : dictionary) {
			String[] allMangles = Mangles.callAllFunc(word);
			for (String mangles : allMangles) {
				for (String hash : PasswordCrack.passHash) {
					comparison(mangles, hash);
				}
			}
		}
		//two mangle applied at the same time to words
		for (String word : dictionary) {
			String[] allMangles = Mangles.callAllFunc(word);
			for (String mangles : allMangles) {
				String[] allMangles2 = Mangles.callAllFunc(mangles);
				for (String mangles2 : allMangles2) {
					for (String hash : PasswordCrack.passHash) {
						comparison(mangles2, hash);
					}
				}
			}
		}
	}

	public static void comparison(String word, String hash) {
		String salt = hash.substring(0, 2);
		String dicHash = jcrypt.crypt(salt, word);
		if (dicHash.equals(hash)) {
			System.out.println(word);
			PasswordCrack.passHash.remove(hash);
		}
	}

}

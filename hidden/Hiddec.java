import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Hiddec {

    //To read the input file
    public static byte[] readFile(String data) {
        try {
            return Files.readAllBytes(Path.of(data));
        } catch (IOException e) {
            System.out.println("Error: The input file cannot be read.");
            System.exit(1);
        } catch (NullPointerException e) {
            System.out.println("Error: There needs to be an argument containing the input file in the form of '--input=INPUT'.");
            System.exit(1);
        }
        return new byte[0];
    }

    //To write to the output file
    public static void writeFile(byte[] data, String name) {
        try {
            Files.write(Path.of(name), data);
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Error: The output file cannot be written.");
            System.exit(1);
        } catch (NullPointerException e) {
            System.out.println("Error: There needs to be an argument containing the output file name in the form of '--output=OUTPUT'.");
            System.exit(1);
        }
    }

    //To get the value of the argument using their "keys"
    public static Map<String, String> mapArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (String arg : args) {
            String[] split = arg.split("=");
            if (split.length != 2) {
                System.out.println("Error: Invalid argument : " + Arrays.toString(split));
                System.exit(1);
            }
            arguments.put(split[0], split[1]);
        }
        return arguments;
    }

    //To turn the hex String into a byte array
    //ONLY THIS SECTION is taken from this link:
    //https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java
    public static byte[] hexStringToByteArray(String s) {
        if (s == null) {
            System.out.println("Error: There needs to be an argument containing the key in the form of '--key=KEY'.");
            System.exit(1);
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    //To create message digest / hashed value
    //This part is inspired by the second assignment in module 1
    public static byte[] createHash(byte[] byteArray) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: Algorithm is not available");
            System.exit(1);
        }
        return md.digest(byteArray);
    }

    public static Cipher createCipher(byte[] key, byte[] ctr) {
        Cipher cipher = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            if (ctr != null) { //used for task 3
                IvParameterSpec iv = new IvParameterSpec(ctr);
                cipher = Cipher.getInstance("AES/CTR/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            } else { //used for task 1
                cipher = Cipher.getInstance("AES/ECB/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException e) {
            System.out.println("Error: Failed to get cipher");
            System.exit(1);
        }
        return cipher;
    }

    //To decrypt
    public static byte[] decrypt(byte[] block) {
        try {
            return cipher.doFinal(block);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Error: Failed to decrypt block");
            System.exit(1);
        }
        return new byte[0];
    }

    //To find the first H(K)
    public static int getIndexOfHk1(byte[] input, byte[] hashedKey) {
        for (int i = 0; i < input.length; i += 16) {
            byte[] block = Arrays.copyOfRange(input, i, i + 16);
            byte[] decrypt = decrypt(block);

            if (Arrays.equals(decrypt, hashedKey)) {
                return i;
            }
        }
        System.out.println("Failed to find H(k)");
        System.exit(1);
        return -1;
    }

    public static Cipher cipher;

    public static void main(String[] args) {
        Map<String, String> arguments = mapArguments(args);

        byte[] input = readFile(arguments.get("--input"));
        byte[] key = hexStringToByteArray(arguments.get("--key"));
        byte[] hashedKey = createHash(key);

        byte[] ctr = null;
        if (arguments.containsKey("--ctr")) {
            ctr = hexStringToByteArray(arguments.get("--ctr"));
        }
        cipher = createCipher(key, ctr);

        int indexOfHk1 = getIndexOfHk1(input, hashedKey);

        //removes everything before the first H(K)
        byte[] trimmedInput = Arrays.copyOfRange(input, indexOfHk1, input.length);

        for (int i = 48; i < trimmedInput.length; i += 16) {
            byte[] decryptedBlob = decrypt(Arrays.copyOfRange(trimmedInput, 0, i));

            //the last block (16 bytes) is the H(data)
            byte[] decryptedHData = Arrays.copyOfRange(decryptedBlob, decryptedBlob.length - 16, decryptedBlob.length);

            //the second to last block is the 2nd H(K)
            byte[] Hk = Arrays.copyOfRange(decryptedBlob, decryptedBlob.length - 32, decryptedBlob.length - 16);

            //"data" is after the 1st block (which is 1st H(K)) and before the last 2 blocks (2nd H(K) and H(data))
            byte[] data = Arrays.copyOfRange(decryptedBlob, 16, decryptedBlob.length - 32);

            byte[] hashedData = createHash(data);

            if (Arrays.equals(decryptedHData, hashedData) && Arrays.equals(Hk, hashedKey)) {
                writeFile(data, arguments.get("--output"));
            }

        }
        System.out.println("Error: Failed to find data");
        System.exit(1);
    }
}

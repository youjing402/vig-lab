import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;


public class Encryptor {
	public char[] readFile () {
		try {
			String fileName = "plaintext.txt";
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			char[] chars;
			line = reader.readLine();
			reader.close();
			chars = line.toCharArray();
			return chars;
		} catch (Exception e) {
			return new char[0];
		}
	}
	
	public String encrypt (String plaintext, String key) {
		int size = plaintext.length();
		//System.out.println(size);
		char[] encryptedtext = new char[size];
		Character[] characterList = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'å', 'ä', 'ö', ' ', ',', '.'};
		for (int i=0; i<size; i++) {
			char character = plaintext.charAt(i);
			char keyChar = key.charAt(i % (key.length()));
			int keyIndex = Arrays.asList(characterList).indexOf(keyChar);
			int index = Arrays.asList(characterList).indexOf(character);
			int encryptedIndex = (index + keyIndex) % 32;
			char encryptedChar = characterList[encryptedIndex];
			System.out.println(size);
			encryptedtext[i] = encryptedChar;
		}
		return new String(encryptedtext);
	}
	
	public static void main (String[] args) {
		Encryptor e = new Encryptor();
		//char[] a1 = "varsågod".toCharArray();
		String encryptedString = e.encrypt("Stora delar av brons stenar demonterades och byttes vid behov, bland annat avlägsnades samtliga järndubbar som blivit rostiga. Ett stort problem var ledningsdragningarna över bron. Det rörde sig om många eftersom här gick och går huvudstråket av ledningar för vatten, el, gas, fjärrvärme, telefon och andra medier mellan Gamla stan och Norrmalm. I körbanan byttes samtliga rörledningar, hela ytan under körbanan är täckt med skyddsrör av stål. Efter ett omfattande förstärkningsarbete med bland annat installation av över jetpelare som står förankrade i Brunkebergsåsens grus skall bron hålla i många år till.".toLowerCase(), "lala");
		System.out.println(encryptedString);
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("outfilename"), "UTF-8"));
			try {
			    out.write(encryptedString);
			} finally {
			    out.close();
			}
		} catch (Exception exc) {
			
		}
	}
}

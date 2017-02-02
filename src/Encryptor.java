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
			//System.out.println(index);
			char encryptedChar = characterList[encryptedIndex];
			//System.out.println(encryptedChar);
			//System.out.println(keyChar);
			System.out.println(size);
			encryptedtext[i] = encryptedChar;
		}
		return new String(encryptedtext);
	}
	
	public static void main (String[] args) {
		Encryptor e = new Encryptor();
		//char[] a1 = "varsågod".toCharArray();
		String encryptedString = e.encrypt("Vid geomagnetiska stormar, då polarskensovalen störs och växer i storlek, kan man se norrsken även vid sydligare latituder som mellersta och södra Sverige och i undantagsfall ända ner i Sydeuropa eller till och med i Afrika. På samma sätt expanderar söderskensovalen norrut vid sådana tillfällen.".toLowerCase(), "morgon");
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

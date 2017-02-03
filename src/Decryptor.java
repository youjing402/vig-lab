import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Decryptor {
	public static Character[] characterList = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'å', 'ä', 'ö', ' ', ',', '.'};
	
	
	/**
	 * Convert a string to the corresponding character code array
	 * @param text
	 * @return code array
	 */
	public int[] stringToCode (String text) {
		int[] code = new int[text.length()];
		for (int i=0; i<text.length(); i++) {
			char character = text.charAt(i);
			int charIndex = Arrays.asList(characterList).indexOf(character);
			code[i] = charIndex;
		}
		return code;
	}
	
	
	/**
	 * Guess the key length given ciphertext
	 * @param ciphertext
	 * @return the most possible key length
	 */
	public int guessKeyLength(String ciphertext) {
		KeyLengthGuesser guesser = new KeyLengthGuesser();
		ArrayList<Integer> keys = guesser.guess(ciphertext);
		return keys.get(0);
	}
	
	
	/**
	 * Calculate the key given key length and ciphertext
	 * @param keyLength
	 * @param ciphertext
	 * @return
	 */
	public String calculateKey (int keyLength, String ciphertext) {
		int[] ciphercode = stringToCode(ciphertext);
		double[] frequencyList = {0.0938, 0.0154, 0.0149, 0.047, 0.1015, 0.0203, 0.0286, 0.0209, 0.0582, 0.0061, 0.0314, 0.0528, 0.0347, 0.0854, 0.0448, 0.0184, 0.0002, 0.0843, 0.0659, 0.0769, 0.0192, 0.0242, 0.0014, 0.0016, 0.0071, 0.0007, 0.0134, 0.018, 0.0131};
		double[][] allShiftCharFrequency = new double[keyLength][frequencyList.length];
		
		for (int i=0; i<keyLength; i++) {
			int[] charNumber = new int[characterList.length];
			Arrays.fill(charNumber, 0);
			int charLength = 0;
			for (int j=i; j<ciphercode.length; j+=keyLength) {
				if(ciphercode[j] < 29) {
					charLength++;
					int charIndex = ciphercode[j];
					charNumber[charIndex]++;
				}
			}
			for (int m=0; m<frequencyList.length; m++) {
				allShiftCharFrequency[i][m] = charNumber[m] * 1.0 / charLength;
			}
		}
		
		int[] shiftList = new int[keyLength];
		// each group loop
		// Calculate the character on every position in the key
		for (int m=0; m<keyLength; m++) {
			double maxScore = 0;
			int maxShift = 0;
			// each shift loop
			// Obtain the maximum score and the respective shift
			for (int i=0; i<characterList.length; i++) {
				double score = 0;
				int count = 0;
				// each letter loop. 
				// Calculate and sum up the score for all letters (excluding punctuation)
				for (int j=0; j<frequencyList.length; j++) {
					int modIndex = (i + j) % characterList.length;
					if (modIndex < 29) {
						count ++;
						score += frequencyList[modIndex] * allShiftCharFrequency[m][j];
					}
				}
				score = score / count;
				if (score > maxScore) {
					maxScore = score;
					maxShift = i;
				}
			}
			shiftList[m] = maxShift;
		}
		
		char[] key = new char[keyLength];
		for (int i=0; i<keyLength; i++) {
			key[i] = characterList[(32 - shiftList[i]) % 32];
		}
		
		System.out.println(new String(key));
		return new String(key);
	}
	
	
	/**
	 * Decrypt the ciphertext by shifting the text according to the key
	 * @param key
	 * @param ciphertext
	 * @return
	 */
	public String shiftByKey (String key, String ciphertext) {
		int size = ciphertext.length();
		char[] decryptedtext = new char[size];
		for (int i=0; i<size; i++) {
			char character = ciphertext.charAt(i);
			char keyChar = key.charAt(i % (key.length()));
			int keyIndex = Arrays.asList(characterList).indexOf(keyChar);
			int index = Arrays.asList(characterList).indexOf(character);
			int decryptedIndex = (index + 32 - keyIndex) % 32;
			char decryptedChar = characterList[decryptedIndex];
			decryptedtext[i] = decryptedChar;
		}
		return new String(decryptedtext);
	}
	
	
	/**
	 * Decrypt a given ciphertext by guessing key length, calculating the key, and finally shifting 
	 * the ciphertext
	 * @param ciphertext
	 * @return
	 */
	public String decrypt (String ciphertext) {
		int keyLength = guessKeyLength(ciphertext);
		String key = calculateKey(keyLength, ciphertext);
		String decryptedtext = shiftByKey(key, ciphertext);
		return decryptedtext;
	}
	
	
	/**
	 * Read a file to string
	 * @param fileName
	 * @return
	 */
	public String readFile (String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			line = reader.readLine();
			reader.close();
			return line;
		} catch (Exception e) {
			return "";
		}
	}
	
	
	public static void main (String[] args) {
		//String ciphertext = e.encrypt("Stora delar av brons stenar demonterades och byttes vid behov, bland annat avlägsnades samtliga järndubbar som blivit rostiga. Ett stort problem var ledningsdragningarna över bron. Det rörde sig om många eftersom här gick och går huvudstråket av ledningar för vatten, el, gas, fjärrvärme, telefon och andra medier mellan Gamla stan och Norrmalm. I körbanan byttes samtliga rörledningar, hela ytan under körbanan är täckt med skyddsrör av stål. Efter ett omfattande förstärkningsarbete med bland annat installation av över jetpelare som står förankrade i Brunkebergsåsens grus skall bron hålla i många år till.".toLowerCase(), "kolpwpwlbwsp");
		//String ciphertext = d.readFile(fileName);
		/*
		Encryptor e = new Encryptor();
		String ciphertext = "aio rezmlgye,i kl  tzrxaö,idf åowaösveyszvllpnis,öösionhivgxpriiis,oölpkj vay xay  einzrösvey gvpnivtdisddwiraöeillttt.dpriszmimplweös,aionhishdöaisaeöireionhiiiuydln,arsqawliäydl yeö t  yoe.rzpl plweö ,iwlionhimpdiiiaqrtkl.ipf  axml  ä,tiecplnoeöaö  öoeösveyszvllpninzröu, aio  åoayaittlwfglwey.";
		String key = d.calculateKey(keyLength, ciphertext);
		System.out.println(key);
		System.out.println(ciphertext);
		*/
		Decryptor d = new Decryptor();
		String fileName = args[0];
		String ciphertext = d.readFile(fileName);
		String plaintext = d.decrypt(ciphertext);
		System.out.println(plaintext);
	}
}

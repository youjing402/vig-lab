import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class Decryptor {
	public static Character[] characterList = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'å', 'ä', 'ö', ' ', ',', '.'};
	
	public int[] stringToCode (String text) {
		int[] code = new int[text.length()];
		for (int i=0; i<text.length(); i++) {
			char character = text.charAt(i);
			int charIndex = Arrays.asList(characterList).indexOf(character);
			code[i] = charIndex;
		}
		return code;
	}
	
	public int guessKeyLength(String ciphertext) {
		return 2;
	}
	
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
		for (int m=0; m<keyLength; m++) {
			double maxScore = 0;
			int maxShift = 0;
			// each shift loop
			for (int i=0; i<characterList.length; i++) {
				double score = 0;
				int count = 0;
				// each letter loop
				for (int j=0; j<frequencyList.length; j++) {
					count ++;
					int modIndex = (i + j) % characterList.length;
					if (modIndex < 29) {
						score += frequencyList[modIndex] * allShiftCharFrequency[m][j];
					}
				}
				score = score / count;
				System.out.println(score);
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
		
		return new String(key);
	}
	
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
	
	public String decrypt (String ciphertext) {
		int keyLength = guessKeyLength(ciphertext);
		String key = calculateKey(keyLength, ciphertext);
		String decryptedtext = shiftByKey(key, ciphertext);
		return decryptedtext;
	}
	
	public String readFile (String fileName) {
		try {
			//fileName = "plaintext.txt";
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
		Encryptor e = new Encryptor();
		Decryptor d = new Decryptor();
		String ciphertext = e.encrypt("Stora delar av brons stenar demonterades och byttes vid behov, bland annat avlägsnades samtliga järndubbar som blivit rostiga. Ett stort problem var ledningsdragningarna över bron. Det rörde sig om många eftersom här gick och går huvudstråket av ledningar för vatten, el, gas, fjärrvärme, telefon och andra medier mellan Gamla stan och Norrmalm. I körbanan byttes samtliga rörledningar, hela ytan under körbanan är täckt med skyddsrör av stål. Efter ett omfattande förstärkningsarbete med bland annat installation av över jetpelare som står förankrade i Brunkebergsåsens grus skall bron hålla i många år till.".toLowerCase(), "ko");
		//String ciphertext = d.readFile(fileName);
		/*
		String ciphertext = "aio rezmlgye,i kl  tzrxaö,idf åowaösveyszvllpnis,öösionhivgxpriiis,oölpkj vay xay  einzrösvey gvpnivtdisddwiraöeillttt.dpriszmimplweös,aionhishdöaisaeöireionhiiiuydln,arsqawliäydl yeö t  yoe.rzpl plweö ,iwlionhimpdiiiaqrtkl.ipf  axml  ä,tiecplnoeöaö  öoeösveyszvllpninzröu, aio  åoayaittlwfglwey.";
		String key = d.calculateKey(keyLength, ciphertext);
		System.out.println(key);
		System.out.println(ciphertext);
		*/
		String plaintext = d.decrypt(ciphertext);
		System.out.println(plaintext);
	}
}

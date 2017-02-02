import java.io.BufferedWriter;
import java.io.FileOutputStream;
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
		
	}
	
	public String calculateKey (int keyLength, String ciphertext) {
		
		int[] ciphercode = stringToCode(ciphertext);
		double[] frequencyList = {0.1004, 0.0131, 0.0171, 0.049, 0.0985, 0.0181, 0.0344, 0.0285, 0.0501, 0.009, 0.0324, 0.0481, 0.0355, 0.0845, 0.0406, 0.0157, 0.0001, 0.0788, 0.0532, 0.0889, 0.0186, 0.0255, 0.0011, 0.0049, 0.0004, 0.0166, 0.021, 0.015};
		//System.out.println(frequencyList.length);
		double[][] allShiftCharFrequency = new double[keyLength][frequencyList.length];
		
		for (int i=0; i<keyLength; i++) {
			int[] charNumber = new int[characterList.length];
			Arrays.fill(charNumber, 0);
			int charLength = 0;
			for (int j=i; j<ciphercode.length; j+=keyLength) {
				if(ciphercode[j] < 28) {
					charLength++;
					int charIndex = ciphercode[j];
					charNumber[charIndex]++;
				}
			}
			for (int m=0; m<frequencyList.length; m++) {
				allShiftCharFrequency[i][m] = charNumber[m] * 1.0 / charLength;
				//System.out.println(allShiftCharFrequency[i][m]);
			}
			//System.out.println("");
		}
		
		int[] shiftList = new int[keyLength];
		// each group loop
		for (int m=0; m<keyLength; m++) {
			double maxScore = 0;
			int maxShift = 0;
			// each shift loop
			for (int i=0; i<characterList.length; i++) {
				double score = 0;
				// each letter loop
				for (int j=0; j<frequencyList.length; j++) {
					int modIndex = (i + j) % characterList.length;
					if (modIndex < 28) {
						//System.out.println(frequencyList.length);
						//System.out.println(modIndex);
						score += frequencyList[modIndex] * allShiftCharFrequency[m][j];
						//System.out.println(score);
						//System.out.println(maxScore);
					}
				}
				System.out.println(score);
				if (score > maxScore) {
					maxScore = score;
					maxShift = i;
					//System.out.println(maxShift);
				}
			}
			shiftList[m] = maxShift;
			//System.out.println(maxScore);
			//System.out.println("");
			//System.out.println(maxShift);
		}
		
		char[] key = new char[keyLength];
		for (int i=0; i<keyLength; i++) {
			key[i] = characterList[(32 - shiftList[i]) % 32];
		}
		
		return new String(key);
		//return maxShift;
	}
	
	public static void main (String[] args) {
		Decryptor d = new Decryptor();
		int keyLength = 6;
		String ciphertext = "bwuduråårmär.wdqok,b.xån mojikäöög..ws,yöcmzvtl..kcyläovoäjeq.ool..öcrsxklägäkyo,darjä.x..ws,djcqäoäwqjajjzvsocklymbzzcqq.oyözjåvrzr aegläovoykq ooydr wxkläovoolbzrrtbnsawgzyjj,jokzscdwk,gukc,å rdsyxscdbvxzouqujåvjlvjowxwxmnovik,o sok,jezlrd rtrr ocdajpscyyrza.äoyqäotö, ceddvpldarnzoozwyxtmrzrzn";
		String key = d.calculateKey(keyLength, ciphertext);
		System.out.println(key);
	}
}

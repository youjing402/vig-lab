import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class KeyLengthGuesser {
	public ArrayList<Integer> guess(String text){
		HashMap<Integer,Double> guesses = new HashMap<Integer, Double>();
		for(int i = 2; i <= 635; i++){
			ArrayList<String> everyStr = this.strToEvery(i, text);
			Double avg = 0.0;
			for(int es = 0; es < everyStr.size(); es++){
				int len = everyStr.get(es).length();
				HashMap<Character, Integer> occurrences = this.occurrences(everyStr.get(es));
				Double IoC = this.IoC(occurrences, len);
				avg = avg + IoC;
			}
			Double new_ioc = Math.abs(0.068 - (avg/i));
			guesses.put(i, new_ioc);
		}
		
		Map<Integer,Double> sorted = new TreeMap(new ValueComparator(guesses));
		sorted.putAll(guesses);
		
		ArrayList<Integer> ret = new ArrayList<Integer>(sorted.keySet());
		
		return ret;
	}
	
	public ArrayList<String> strToEvery(int every, String str){
		ArrayList<String> arr = new ArrayList<String>();
		
		for(int j = 0; j < every; j++){
			String substr = "";
			for(int i = 0; i < str.length(); i++){
				substr = substr + (i % every == j ? str.charAt(i) : "" );
			}
			arr.add(substr);
		}
		return arr;
	}
	
	public HashMap<Character, Integer> occurrences(String text){
		HashMap<Character, Integer> arr = new HashMap<Character, Integer>();
		for(int i = 0; i < text.length(); i++){
			char letter = text.charAt(i);
			if(arr.containsKey(letter)){
				arr.put(letter, arr.get(letter)+1);
			} else {
				arr.put(letter, 1);
			}
		}
		
		return arr;
	}
	
	public Double IoC(HashMap<Character, Integer> occurrences, int len){
		int sum = 0;
		for(int count : occurrences.values()){
			sum = sum + (count * (count - 1));
			
		}
		Double ret = (double)sum/(len * (len-1));
		return ret;
	}
	
	public static void main (String[] args) {
		KeyLengthGuesser klg = new KeyLengthGuesser();
		ArrayList<Integer> guesses = klg.guess("bwudur.ådtnhxvewooybäcåd,,ahhpäöög..äsq.oyeywzldzk,dlrphazijq.ool.eöuyenckömäosoåoahknrv,ews,djcväacigb.kpzzmo,vlontlxbvq.oyözoåhylhv.fml.ivkdkg,aawcw wxklätvav xrqszbrmasrzokäqhnpzscdwkdggruusösjsörs,oblylaspzjåvjlvooi,inempäioyoz oa.äwxkwd rtrrcouks.hrd,yvtaägoornaräd ceddvulvhddrnp.wörtiözhå.");
		for(int i = 0; i < guesses.size()
				; i++){
			System.out.println(guesses.get(i));
		}
	}
}

class ValueComparator implements Comparator {
	HashMap map;
	
	public ValueComparator(HashMap map){
		this.map = map;
	}
	
	public int compare(Object keyA, Object keyB){
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueA.compareTo(valueB);
	}
}
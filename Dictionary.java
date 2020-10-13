
import java.util.TreeMap;


public class Dictionary {
    TreeMap<String, String> words = new TreeMap<>();
    int count = 0;

    public void addWord(String target, String explain){
        words.put(target,explain);
        count++;
    }


}


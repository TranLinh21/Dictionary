
import java.util.TreeMap;


public class Dictionary {
    TreeMap<String, String> words = new TreeMap<>();
    public void addWord(String target, String explain) {
        words.put(target, explain);
    }


}


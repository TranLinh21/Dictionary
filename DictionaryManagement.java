import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class DictionaryManagement {
    public static Dictionary dic = new Dictionary();

    private static Scanner sc = new Scanner(System.in);

    public void insertFromCommandline() {
        System.out.println("Enter number of words: ");
        int num = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < num; i++) {
            System.out.println("Enter target word: ");
            String target = sc.nextLine();
            System.out.println("Enter explain word: ");
            String explain = sc.nextLine();
            dic.addWord(target, explain);
        }
    }

    public void insertFromFile() throws  Exception {
        String fileName = "dictionaries.txt";
        BufferedReader bReader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = bReader.readLine()) != null) {
            String[] data;
            data = line.split("\t");
            String value1 = data[0];
            StringBuilder value2 = new StringBuilder( data[1] + "\n");
            while((line = bReader.readLine()) != null){
                if(line.equals("")){
                    break;
                }
                value2.append(line).append("\n");
            }
            dic.addWord(value1, value2.toString());
        }
        bReader.close();
    }

    public void showAllWords() {
        System.out.println("NO" + "\t" + "|English" + "\t\t\t\t" + "|Vietnamese");
        int i = 0;
        for (String key : dic.words.keySet()) {
            System.out.println((i + 1) + "\t|" + key+ "\t\t\t\t\t|" + dic.words.get(key));
        }
    }

    public String dictionaryLookup(String keyword) {
       return dic.words.getOrDefault(keyword,"Word not found !!!");
    }
    public void removeWord(String word) {
      dic.words.remove(word);
    }

    public void modifyWord( String key, String newExplainWord) {
        dic.words.replace(key, newExplainWord);
        System.out.println(dic.words.get(newExplainWord));
    }

    public String[] dictionarySearcher(String word) {
        if (word.equals("")) return dic.words.keySet().toArray(new String[0]);
        String startKey = "";
        String endKey = "";
        for (String key : dic.words.keySet()) {
            if (key.length() >= word.length() && (key.substring(0, word.length()).equals(word))) {
                    if (startKey.equals("")) {
                        startKey = key;
                    }
                }
                else if (!startKey.equals("")) {
                    endKey = key;
                    break;
                }
            }

        return dic.words.subMap(startKey, endKey).keySet().toArray(new String[0]);

    }

    public void dictionaryExportToFile(String fileName) throws Exception {
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileName, true));
        for (String key : dic.words.keySet()) {
            bWriter.write(key + "\t" + dic.words.get(key) + "\n");
        }
        bWriter.close();
    }
}
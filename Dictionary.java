public class Dictionary {
    Word[] words = new Word[1];
    int count = 0;
    public void addWord(String target, String explain){
        if(count == words.length){
            resize(2*words.length);
        }
        words[count] = new Word(target, explain);
        count++;
    }

    public void resize(int capacity){
        Word[] copy = new Word[capacity];
        for (int i = 0; i < count; i++)
            copy[i] = words[i];
        words = copy;
    }
}


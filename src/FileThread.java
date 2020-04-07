import java.io.*;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class FileThread implements Callable {
    HashMap<String,Integer> map;
    File file;
    int x;


    public FileThread(){ }

    public FileThread(String s, int x, HashMap<String,Integer> map){
        this.file = new File(s);
        this.map = map;
        this.x = x;
    }

    public Object call(){
        String line;
        int count_words = 0;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
                while ((line = reader.readLine()) != null){

                    if(!(line.equals(""))){
                        line =  line.replaceAll("[^a-zA-Z0-9]", " ");
                        String[] wordList = line.split("\\s+");
                        for(String word: wordList){
                            if(word.length() == 1 || word.equals("")){
                                continue;
                            }
                            word = word.toLowerCase();
                            addWordsToMap(word,map);
                        }
                    }
                }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;

    }

    public static synchronized void addWordsToMap(String word , HashMap<String,Integer> map) {
        if (map.containsKey(word)) {
            int value = map.get(word);
            ++value;
            map.put(word, value);
        } else {
            map.put(word, 1);
        }

    }
}

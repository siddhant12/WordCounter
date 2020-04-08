import java.io.*;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class WordCounter {

    static HashMap<String,Integer> map= new HashMap<>();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        URL url = new URL("http://www.gutenberg.org/files/2600/2600-0.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        char[] charArray = new char[100000];
        int x=1, j=0;
        String s  ="";
        int read_bytes;
        List<Callable<FileThread>> myThreads = new ArrayList<>();
        ArrayList<String> fileArray = new ArrayList<>();
        while(reader.readLine() !=null ){
            j = 0;
            s = "";
            if(x<9){
                s =  "text-file-split" + x+".txt";
            }else{
                s =  "text-file-split" + x+".txt";
            }
            fileArray.add(s);

            FileOutputStream fos = new FileOutputStream(s);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);

            while(j < 500000 && reader.readLine() !=null){
                read_bytes = reader.read(charArray , 0 , 100000);
                j = j + read_bytes;
                byte[] byteArray = Charset.forName("UTF-8").encode(CharBuffer.wrap(charArray)).array();
                fos.write(byteArray , 0 , read_bytes);
            }
            FileThread fileThread = new FileThread(s,x,map);
            myThreads.add(fileThread);
            x++;
        }
        int totalFiles = myThreads.size();
        ExecutorService service = Executors.newFixedThreadPool(totalFiles);
        service.invokeAll(myThreads);
        service.shutdown();
        WordCounter mapTest = new WordCounter();
        mapTest.mostFrequentlyUsedWords(map);
        mapTest.deleteFiles(fileArray);

    }

    private void deleteFiles(ArrayList<String> fileArray) {
        for(String file: fileArray){
            File f = new File(file);
            f.delete();
        }
    }

    private void mostFrequentlyUsedWords(HashMap<String, Integer> resultMap) {

        int totalWords;

        final Map<String, Integer> sortedByCount = resultMap.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        totalWords = sortedByCount.values().stream().mapToInt(i -> i).sum();
        System.out.println("How many times word has appeared in documnet: " + totalWords);
        System.out.println("\nHow many times word has appeared in documnet");

        sortedByCount.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });

        System.out.println("\nTop Five Words used are");
        int i = 0;

        List<Map.Entry<String,Integer>> topFive = sortedByCount.entrySet().stream().limit(5).collect(Collectors.toList());
        System.out.println(topFive);
    }

}
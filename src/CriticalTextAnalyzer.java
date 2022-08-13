import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class CriticalTextAnalyzer {

    private StringTokenizer splitter;

    private List<String> fileDataByLines;

    public CriticalTextAnalyzer (String fileName) {
        readTextFile(fileName);
    }

    private void readTextFile (String fileName){
        fileDataByLines = new ArrayList<>();
        try {
            Reader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = null;

            while((line = br.readLine()) != null){
                fileDataByLines.add(line);
                //fileDataByLines.add("~");
            }

        }
        catch (IOException ioe){
            System.out.println("File not found. " + ioe);
        }
        catch (Exception e){
            System.out.println("Error in loading data from the file. " + e);
        }
    }

    public int getNumTokens(){
        String fileData = "";

        for(String line : fileDataByLines){
            fileData += line;
        }
        //String delims = " \t\n,;{}[]().-<>&^%$@!-+/*~=";
        String delims = " {,}";
        splitter = new StringTokenizer(fileData, delims, true);

        while(splitter.hasMoreElements()){
            System.out.println(splitter.nextToken());
            if(splitter.nextToken().trim().equalsIgnoreCase("\n")){
                System.out.println("ddd");

            }
        }
        return splitter.countTokens();
    }


    public void showDataFile(){
        System.out.println(fileDataByLines.size());
        for(String h: fileDataByLines){
            System.out.println(h.trim());
        }
        //System.out.println(fileDataByLines.get(5).trim());
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class XCriticalTextAnalyzer {

    private StringTokenizer splitter;
    private List<String> fileDataByLines;
    private List<String> commentList;
    private List<String> classList;

    private Map<String, Integer> variableMap;

    public XCriticalTextAnalyzer(String fileName) {
        commentList = new ArrayList<>();
        classList = new ArrayList<>();
        variableMap = new HashMap<>();
        variableMap.put("int", 0);
        variableMap.put("char", 0);
        variableMap.put("byte", 0);
        variableMap.put("short", 0);
        variableMap.put("long", 0);
        variableMap.put("double", 0);
        variableMap.put("float", 0);
        variableMap.put("boolean", 0);

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
                fileDataByLines.add(" ");
            }
        }
        catch (IOException ioe){
            System.out.println("File not found. " + ioe);
        }
        catch (Exception e){
            System.out.println("Error in loading data from the file. " + e);
        }
    }

    public void startTextAnalyzer(){
        SplitText(fileDataByLines);
        removeComments();
        SplitText(fileDataByLines);
        findClass();
        SplitText(fileDataByLines);
        findVariables();
        SplitText(fileDataByLines);
    }

    private void SplitText(List<String> data){
        String fileData = "";
        for(String line : data){
            fileData += line;
        }
        String delims = " \t\n,;{}[]().-<>&^%$@!-+/*~=";
        splitter = new StringTokenizer(fileData, delims, true);
    }

    public int getNumTokens(){
        return splitter.countTokens();
    }

    private void removeComments(){

        List<Integer> listOfIndex = new ArrayList<>();
        int count = 0;
        boolean commentFound = false;

        //remove all /**/ comments, /**/ within a line
        count=0;
        commentFound = false;
        String tempComment = "";

        for(String line : fileDataByLines){

            if (line.trim().contains("/*") && !commentFound) {
                if(!line.trim().contains("*/")){
                    fileDataByLines.set(count, line.substring(0,line.indexOf("/*")));
                    commentFound = true;
                    tempComment = line.substring(line.indexOf("/*"));
                }
                else if(line.trim().contains("*/")){
                    commentFound = false;
                    fileDataByLines.set(count, line.substring(line.indexOf("*/")+2));
                    tempComment = line.substring(line.indexOf("/*"), line.indexOf("*/")+2);
                }
            }
            else if (line.trim().contains("*/") && commentFound) {
                fileDataByLines.set(count, line.substring(line.indexOf("*/")+2));
                commentFound = false;
                tempComment += line.substring(0, line.indexOf("*/")+2);
            }
            else if (commentFound) {
                listOfIndex.add(count);
                tempComment += line;
            }
            if(!commentFound && !(tempComment.equalsIgnoreCase(""))){
                commentList.add(tempComment);
                tempComment = "";
            }
            count++;
        }
        reCreateArray(listOfIndex);

        //remove all single comments,  lines starts with "//" and contains with "//"
        count=0;
        listOfIndex = new ArrayList<>();
        for(String line : fileDataByLines){
            if(line.trim().startsWith("//") ){
                listOfIndex.add(count);
                commentList.add(line);
            } else if (line.trim().contains("//")) {
                fileDataByLines.set(count, line.substring(0,line.indexOf("/")));
                commentList.add(line.substring(line.indexOf("/")));
            }
            count++;
        }
        reCreateArray(listOfIndex);
    }

    private void reCreateArray(List<Integer> listOfIndex){
        int count = 0;
        for(int index : listOfIndex) {
            fileDataByLines.remove((index - count));
            count++;
        }
    }

    private void findClass(){
        while(splitter.hasMoreTokens()){
            label_1:
            if(splitter.nextToken().trim().equalsIgnoreCase("class")){
                while(true){
                    String className = splitter.nextToken().trim();
                    if(!className.equals("")){
                        classList.add(className);
                        break label_1;
                    }
                }
            }
        }
    }

    private void findVariables(){

        while(splitter.hasMoreTokens()){
            label_1:
            if(splitter.nextToken().trim().equalsIgnoreCase("int")){
                while(true){
                    String variableName = splitter.nextToken().trim();
                    if(!variableName.equals("")){
                        variableMap.replace("int", variableMap.get("int")+1);;
                        break label_1;
                    }
                }
            }
        }
    }
    public void showComments() {
        System.out.println("------------------------------");
        System.out.println("No of Comments Found : " + commentList.size());
        System.out.println("------------------------------");
        for (String h : commentList) {
            System.out.println("Comment : " + h.trim());
        }
        System.out.println("==============================");
    }
    public void showClass() {
        System.out.println("------------------------------");
        System.out.println("No of Class Found : " + classList.size());
        System.out.println("------------------------------");
        for (String h : classList) {
            System.out.println("Name : " + h.trim());
        }
        System.out.println("==============================");
    }
    public void showVariables() {
        System.out.println("------------------------------");
        System.out.println("No of Variables Found : " + "~");
        System.out.println("------------------------------");
            System.out.println("int : " + variableMap.get("int"));
            System.out.println("char : " + variableMap.get("char"));
            System.out.println("byte : " + variableMap.get("byte"));
            System.out.println("short : " + variableMap.get("short"));
            System.out.println("long : " + variableMap.get("long"));
            System.out.println("double : " + variableMap.get("double"));
            System.out.println("float : " + variableMap.get("float"));
            System.out.println("boolean : " + variableMap.get("boolean"));
        System.out.println("==============================");
    }

    public void showDataFile(){
        System.out.println(fileDataByLines.size());
        for(String h: fileDataByLines){
            System.out.println(h.trim());
        }
    }
    public void showSplitter(){
        while (splitter.hasMoreTokens()){
            System.out.println(splitter.nextToken());
        }
    }
}

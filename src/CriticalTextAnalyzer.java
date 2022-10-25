import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class CriticalTextAnalyzer {

    private final String fileName;

    public CriticalTextAnalyzer(String fileName) {
        this.fileName = fileName;
    }

    private List<String> readTextFile (String fileName){

        List<String> fileDataByLines = new LinkedList<>();
        Reader fr = null;
        try {
            fr = new FileReader(fileName);
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
        }finally {
            //fr.close();
        }
        return fileDataByLines;
    }

    public void startTextAnalyzer(){
        StringTokenizer splitter;

        List<String> fileDataByLines = readTextFile(fileName);


        splitter = splitText(fileDataByLines);
        showNoTokens(getNumTokens(splitter));

        showComments(splitComments(fileDataByLines));

        splitter = splitText(fileDataByLines);
        showClass(findClass(splitter));

        splitter = splitText(fileDataByLines);
        showVariables(findVariables(splitter));


        splitter = splitText(fileDataByLines);
        showMethods(findMethods(splitter));
    }

    private StringTokenizer splitText(List<String> data){
        String fileData = "";
        for(String line : data){
            fileData += line;
        }
        String delims = " \t\n,;{}[]().-<>&^%$@!-+/*~=";
        return new StringTokenizer(fileData, delims, true);
    }

    private int getNumTokens(StringTokenizer splitter){
        return splitter.countTokens();
    }

    private List<String> splitComments(List<String> fileDataByLines){

        List<String> commentList = new ArrayList<>();
        List<Integer> listOfIndex = new ArrayList<>();
        int count = 0;
        boolean commentFound = false;

        //remove all /**/ comments, /**/ within a line
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
        reCreateArray(listOfIndex, fileDataByLines);

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
        reCreateArray(listOfIndex, fileDataByLines);

        return commentList;
    }

    private void reCreateArray(List<Integer> listOfIndex, List<String> fileDataByLines){
        int count = 0;
        for(int index : listOfIndex) {
            fileDataByLines.remove((index - count));
            count++;
        }
    }

    private List<String> findClass(StringTokenizer splitter){
        List<String> classList = new ArrayList<>();
        boolean foundClass = false;
        String wordText;
        String classDetails="";

        while(splitter.hasMoreTokens()){
            wordText = splitter.nextToken().trim();

            if(wordText.equalsIgnoreCase("class")){
                foundClass = true;
                continue;
            }
            if(!wordText.equals("") && foundClass && !wordText.equalsIgnoreCase("{")){
                classDetails += " " + wordText;
            }

            if(wordText.equalsIgnoreCase("{") && foundClass){
                foundClass = false;
                classList.add(classDetails);
                classDetails = "";
            }

        }

        return classList;
    }

    private Map<String, Integer> findVariables(StringTokenizer splitter){
        Map<String, Integer> variableMap = new HashMap<>();
        Map<String, String> variableNames = new HashMap<>();

        variableMap.put("int", 0);
        variableMap.put("char", 0);
        variableMap.put("byte", 0);
        variableMap.put("short", 0);
        variableMap.put("long", 0);
        variableMap.put("double", 0);
        variableMap.put("float", 0);
        variableMap.put("boolean", 0);

        variableNames.put("int", "");
        variableNames.put("char", "");
        variableNames.put("byte", "");
        variableNames.put("short", "");
        variableNames.put("long", "");
        variableNames.put("double", "");
        variableNames.put("float", "");
        variableNames.put("boolean", "");

        boolean foundVariable = false;
        String variableType = "";
        String tempValue = "";

        while(splitter.hasMoreTokens()){
            tempValue = splitter.nextToken().trim();

            if(tempValue.equalsIgnoreCase("int")){
                foundVariable = true;
                variableType = "int";
                continue;
            }

            if(tempValue.equalsIgnoreCase("char")){
                foundVariable = true;
                variableType = "char";
                continue;
            }
            if(tempValue.equalsIgnoreCase("byte")){
                foundVariable = true;
                variableType = "byte";
                continue;
            }
            if(tempValue.equalsIgnoreCase("short")){
                foundVariable = true;
                variableType = "short";
                continue;
            }
            if(tempValue.equalsIgnoreCase("long")){
                foundVariable = true;
                variableType = "long";
                continue;
            }
            if(tempValue.equalsIgnoreCase("double")){
                foundVariable = true;
                variableType = "double";
                continue;
            }
            if(tempValue.equalsIgnoreCase("float")){
                foundVariable = true;
                variableType = "float";
                continue;
            }
            if(tempValue.equalsIgnoreCase("boolean")){
                foundVariable = true;
                variableType = "boolean";
                continue;
            }


            if(!tempValue.equalsIgnoreCase("") && foundVariable){
                variableMap.replace(variableType, variableMap.get(variableType)+1);

                variableNames.replace(variableType, variableNames.get(variableType) + ", " + tempValue);
                foundVariable = false;
            }

            if(tempValue.equalsIgnoreCase(",") && !variableType.equals("")){
                foundVariable = true;
            }

            if(tempValue.equalsIgnoreCase(";")){
                variableType = "";
            }
        }
        showVariablesNames(variableNames);
        return variableMap;
    }

    private List<String> findMethods(StringTokenizer splitter){
        List<String> methodList = new ArrayList<>();
        boolean foundMethod = false;
        String wordText;
        String methodName="";
        int count = 0;//to remove "if(x >0)"

        while(splitter.hasMoreTokens()){
            wordText = splitter.nextToken().trim();

            if(wordText.equals("") ){
                continue;
            }

            if(wordText.equalsIgnoreCase("void") ||
                    wordText.equalsIgnoreCase("int") ||
                    wordText.equalsIgnoreCase("char") ||
                    wordText.equalsIgnoreCase("byte") ||
                    wordText.equalsIgnoreCase("short") ||
                    wordText.equalsIgnoreCase("long") ||
                    wordText.equalsIgnoreCase("double") ||
                    wordText.equalsIgnoreCase("float") ||
                    wordText.equalsIgnoreCase("boolean")){
                foundMethod = true;
                count++;
                continue;
            }
            if(wordText.equals("=") || wordText.equals(";")) {//just a variable declaration found
                foundMethod = false;
                methodName = "";
                continue;
            }
            if(wordText.equals("(") && foundMethod ){
                methodList.add(methodName);
                foundMethod = false;
                methodName = "";
                continue;
            }

            if(count ==  3){foundMethod = false; count=0;}

            if(!wordText.equals("") && foundMethod ){
                methodName = wordText;
                count++;
            }

        }
        return methodList;
    }

    public void showNoTokens(int noOfTokens) {
        System.out.println("------------------------------");
        System.out.println("No of Tokens Found : " + noOfTokens);
        System.out.println("==============================");
    }
    public void showComments(List<String> commentList) {
        System.out.println("------------------------------");
        System.out.println("No of Comments Found : " + commentList.size());
        System.out.println("------------------------------");
        for (String h : commentList) {
            System.out.println("Comment : " + h.trim());
        }
        System.out.println("==============================");
    }
    public void showClass(List<String> classList) {
        System.out.println("------------------------------");
        System.out.println("No of Class Found : " + classList.size());
        System.out.println("------------------------------");
        for (String h : classList) {
            System.out.println("Name : " + h.trim());
        }
        System.out.println("==============================");
    }
    public void showVariables(Map<String, Integer> variableMap) {
        System.out.println("------------------------------");
        System.out.println("No of Variables Found : " );
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
    public void showVariablesNames(Map<String, String> variableNames) {
        System.out.println("------------------------------");
        System.out.println("Variables Names ");
        System.out.println("------------------------------");

        for(Map.Entry<String, String> variablName : variableNames.entrySet()){
            String tempValue = variablName.getValue();
            if(tempValue.trim().equals("")){
                tempValue = " -";
            }else{
                tempValue = tempValue.substring(1);
            }
            System.out.println(variablName.getKey() + " : " + tempValue);
        }
        System.out.println("==============================");
    }
    public void showMethods(List<String> methodList) {
        System.out.println("------------------------------");
        System.out.println("No of Method(s) Found : " + methodList.size());
        System.out.println("------------------------------");
        for (String h : methodList) {
            System.out.println("Method Name : " + h.trim());
        }
        System.out.println("==============================");
    }
    public void showDataFile(List<String> fileDataByLines){
        System.out.println(fileDataByLines.size());
        for(String h: fileDataByLines){
            System.out.println(h.trim());
        }
    }
    public void showSplitter(StringTokenizer splitter){
        while (splitter.hasMoreTokens()){
            System.out.println(splitter.nextToken());
        }
    }
}

public class Main {
    public  static void main(String[] args) throws Exception {
        String rootPath = "C:\\Users\\hp\\Desktop\\test\\";

        CriticalTextAnalyzer txtAnalyser = new CriticalTextAnalyzer(rootPath+"test.java");

        txtAnalyser.startTextAnalyzer();
        //System.out.println(txtAnalyser.getNumTokens());
        //txtAnalyser.showComments();
        //.showClass();
        //txtAnalyser.showVariables();
        //txtAnalyser.showSplitter();



    }
}
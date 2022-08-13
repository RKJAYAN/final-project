public class Main {
    public  static void main(String[] args) throws Exception {
        String rootPath = "C:\\Users\\hp\\Desktop\\test\\";

        XCriticalTextAnalyzer txtAnalyser = new XCriticalTextAnalyzer(rootPath+"test.java");

        txtAnalyser.startTextAnalyzer();
        //System.out.println(txtAnalyser.getNumTokens());
        //txtAnalyser.showComments();
        //.showClass();
        //txtAnalyser.showVariables();
        //txtAnalyser.showSplitter();



    }
}
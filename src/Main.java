import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String delims = " \t\n,;{}[]().-<>&^%$@!-+/*~=";
String line = "I am a sentence read from a file.";
StringTokenizer splitter = new StringTokenizer(line, delims, true);
int numTokens = splitter.countTokens();
String next = splitter.nextToken();

        System.out.println(numTokens);
        System.out.println(next);

        while(splitter.hasMoreElements()){
            System.out.println(splitter.nextToken());
        }

    }
}
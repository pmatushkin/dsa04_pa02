import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BurrowsWheelerTransform {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    String BWT(String text) {
        StringBuilder result = new StringBuilder();

        // write your code here
        final int textLength = text.length();
        final int lastSymbolIndex = textLength - 1;

        String[] bwtStrings = new String[textLength];

        StringBuilder bwtStringBuilder;
        for (int i = lastSymbolIndex; i >= 0; i--) {
            bwtStringBuilder = new StringBuilder(textLength);

            bwtStringBuilder.append(text.substring(lastSymbolIndex));
            bwtStringBuilder.append(text.substring(0, lastSymbolIndex));

            text = bwtStringBuilder.toString();
            bwtStrings[lastSymbolIndex - i] = text;
        }

//        print(bwtStrings);

        Arrays.sort(bwtStrings);
        for (String bwtString: bwtStrings) {
            result.append((bwtString.substring(lastSymbolIndex)));
        }

        return result.toString();
    }

    static void print(String[] strings) {
        for(String string: strings) {
            System.out.println(string);
        }
    }

    static public void main(String[] args) throws IOException {
        new BurrowsWheelerTransform().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        System.out.println(BWT(text));
    }
}

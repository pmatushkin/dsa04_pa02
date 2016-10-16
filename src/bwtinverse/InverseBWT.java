import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {
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

    String inverseBWT(String bwt) {
        StringBuilder result = new StringBuilder();

        // write your code here
        final int textLength = bwt.length();

        // Create what essentially is the FirstColumn of the reverse BWT transform
        // split into 4 parts, each part for a separate letter of the alphabet;
        // count the letters to use counts when calculating the letter offsets in the FirstColumn;

        // Here's how letter offsets work: the FirstColumn is ordered, so the first letter A
        // starts with space 1 (the space 0 is taken by the terminator $), and takes up aCount spaces,
        // the second letter C starts with aCount + 1 space and continues for cCount spaces,
        // and so on.

        int aCount = 0;
        int[] aSymbols = new int[textLength];
        int cCount = 0;
        int[] cSymbols = new int[textLength];
        int gCount = 0;
        int[] gSymbols = new int[textLength];
        int tCount = 0;
        int[] tSymbols = new int[textLength];

        for (int i = 0; i < textLength; i++) {
            char symbolAt = bwt.charAt(i);

            switch (symbolAt)
            {
                case 'A': aSymbols[i] = aCount++; break;
                case 'C': cSymbols[i] = cCount++; break;
                case 'G': gSymbols[i] = gCount++; break;
                case 'T': tSymbols[i] = tCount++; break;
            }
        }

        int nextSymbolIndex = 0;
        char symbolAt = bwt.charAt(0);

        for (int i = 1; i < textLength; i++) {
            result.append(symbolAt);

            switch (symbolAt)
            {
                case 'A': nextSymbolIndex = aSymbols[nextSymbolIndex] + 1; break;
                case 'C': nextSymbolIndex = cSymbols[nextSymbolIndex] + 1 + aCount; break;
                case 'G': nextSymbolIndex = gSymbols[nextSymbolIndex] + 1 + aCount + cCount; break;
                case 'T': nextSymbolIndex = tSymbols[nextSymbolIndex] + 1 + aCount + cCount + gCount; break;
            }

            symbolAt = bwt.charAt(nextSymbolIndex);
        }
        result.reverse().append("$");

        return result.toString();
    }

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BWMatching {
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

    // Preprocess the Burrows-Wheeler Transform bwt of some text
    // and compute as a result:
    //   * starts - for each character C in bwt, starts[C] is the first position
    //       of this character in the sorted array of
    //       all characters of the text.
    //   * occ_count_before - for each character C in bwt and each position P in bwt,
    //       occ_count_before[C][P] is the number of occurrences of character C in bwt
    //       from position 0 to position P inclusive.
    private void PreprocessBWT(String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before) {
        // Implement this function yourself
        final int textLength = bwt.length();

        int aCount = 0;
        int[] aSymbols = new int[textLength + 1];
        int cCount = 0;
        int[] cSymbols = new int[textLength + 1];
        int gCount = 0;
        int[] gSymbols = new int[textLength + 1];
        int tCount = 0;
        int[] tSymbols = new int[textLength + 1];

        for (int i = 0; i < textLength; i++) {
            char symbolAt = bwt.charAt(i);

            switch (symbolAt)
            {
                case 'A': {
                    aSymbols[i] = aCount++;
                    cSymbols[i] = cCount;
                    gSymbols[i] = gCount;
                    tSymbols[i] = tCount;
                } break;
                case 'C': {
                    aSymbols[i] = aCount;
                    cSymbols[i] = cCount++;
                    gSymbols[i] = gCount;
                    tSymbols[i] = tCount;
                } break;
                case 'G': {
                    aSymbols[i] = aCount;
                    cSymbols[i] = cCount;
                    gSymbols[i] = gCount++;
                    tSymbols[i] = tCount;
                } break;
                case 'T': {
                    aSymbols[i] = aCount;
                    cSymbols[i] = cCount;
                    gSymbols[i] = gCount;
                    tSymbols[i] = tCount++;
                } break;
                default: {
                    aSymbols[i] = aCount;
                    cSymbols[i] = cCount;
                    gSymbols[i] = gCount;
                    tSymbols[i] = tCount;
                } break;
            }
        }

        aSymbols[textLength] = aCount;
        cSymbols[textLength] = cCount;
        gSymbols[textLength] = gCount;
        tSymbols[textLength] = tCount;

        starts.put('$', 0);
        starts.put('A', aCount == 0 ? -1 : 1);
        starts.put('C', cCount == 0 ? -1 : 1 + aCount);
        starts.put('G', gCount == 0 ? -1 : 1 + aCount + cCount);
        starts.put('T', tCount == 0 ? -1 : 1 + aCount + cCount + gCount);

        occ_counts_before.put('A', aSymbols);
        occ_counts_before.put('C', cSymbols);
        occ_counts_before.put('G', gSymbols);
        occ_counts_before.put('T', tSymbols);
    }

    // Compute the number of occurrences of string pattern in the text
    // given only Burrows-Wheeler Transform bwt of the text and additional
    // information we get from the preprocessing stage - starts and occ_counts_before.
    int CountOccurrences(String pattern, String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before) {
        // Implement this function yourself
        int top = 0;
        int bottom = bwt.length() - 1;

        int index = pattern.length() - 1;
        while (top <= bottom) {
            if (index >= 0) {
                char symbolAt = pattern.charAt(index--);

                if (symbolAt == '$') {
                    continue;
                }

                int[] occ_count = occ_counts_before.get(symbolAt);

                int countAtTop = occ_count[top];
                int countAtBottom = occ_count[bottom + 1];

                if (countAtBottom > countAtTop) {
                    top = starts.get(symbolAt) + countAtTop;
                    bottom = starts.get(symbolAt) + occ_count[bottom + 1] - 1;
                } else {
                    return 0;
                }
            } else {
                return bottom - top + 1;
            }
        }

        return 0;
    }

    static public void main(String[] args) throws IOException {
        new BWMatching().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        // Start of each character in the sorted list of characters of bwt,
        // see the description in the comment about function PreprocessBWT
        Map<Character, Integer> starts = new HashMap<Character, Integer>();
        // Occurrence counts for each character and each position in bwt,
        // see the description in the comment about function PreprocessBWT
        Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
        // Preprocess the BWT once to get starts and occ_count_before.
        // For each pattern, we will then use these precomputed values and
        // spend only O(|pattern|) to find all occurrences of the pattern
        // in the text instead of O(|pattern| + |text|).
        PreprocessBWT(bwt, starts, occ_counts_before);
        int patternCount = scanner.nextInt();
        String[] patterns = new String[patternCount];
        int[] result = new int[patternCount];
        for (int i = 0; i < patternCount; ++i) {
            patterns[i] = scanner.next();
            result[i] = CountOccurrences(patterns[i], bwt, starts, occ_counts_before);
        }
        print(result);
    }
}

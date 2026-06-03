public class WaveletTreeDNA {

    // Root level bit-vector: 0 -> {A, C}, 1 -> {G, T}
    private int[] rootBits;

    // Left child bit-vector: 0 -> A, 1 -> C
    private int[] leftBits;

    // Right child bit-vector: 0 -> G, 1 -> T
    private int[] rightBits;

    private String sequence;

    // ========================
    // CONSTRUCTOR (BUILD TREE)
    // ========================
    public WaveletTreeDNA(String sequence) {
        this.sequence = sequence;
        build();
    }

    // ========================
    // BUILD WAVELET TREE
    // ========================
    private void build() {
        int n = sequence.length();

        rootBits = new int[n];

        // Temporary strings for children
        StringBuilder leftSeq = new StringBuilder();
        StringBuilder rightSeq = new StringBuilder();

        // Root-level encoding
        for (int i = 0; i < n; i++) {
            char ch = sequence.charAt(i);

            if (ch == 'A' || ch == 'C') {
                rootBits[i] = 0;
                leftSeq.append(ch);
            } else { // G or T
                rootBits[i] = 1;
                rightSeq.append(ch);
            }
        }

        // Left child bit-vector (A vs C)
        leftBits = new int[leftSeq.length()];
        for (int i = 0; i < leftSeq.length(); i++) {
            leftBits[i] = (leftSeq.charAt(i) == 'A') ? 0 : 1;
        }

        // Right child bit-vector (G vs T)
        rightBits = new int[rightSeq.length()];
        for (int i = 0; i < rightSeq.length(); i++) {
            rightBits[i] = (rightSeq.charAt(i) == 'G') ? 0 : 1;
        }
    }

    // ========================
    // RANK FUNCTION
    // ========================
    private int rank(int[] bits, int idx, int bit) {
        int count = 0;
        for (int i = 0; i < idx; i++) {
            if (bits[i] == bit)
                count++;
        }
        return count;
    }

    // ========================
    // RANGE-RANK QUERY
    // Count occurrences of 'A'
    // ========================
    public int rangeRankA(int l, int r) {
        // Step 1: root (go left for A => bit 0)
        int newL = rank(rootBits, l, 0);
        int newR = rank(rootBits, r + 1, 0);

        // Step 2: left child (A => bit 0)
        return rank(leftBits, newR, 0) - rank(leftBits, newL, 0);
    }

    // ========================
    // DISPLAY BIT-VECTORS
    // ========================
    public void display() {
        System.out.print("Root bits: ");
        for (int b : rootBits) System.out.print(b + " ");
        System.out.println();

        System.out.print("Left bits (A/C): ");
        for (int b : leftBits) System.out.print(b + " ");
        System.out.println();

        System.out.print("Right bits (G/T): ");
        for (int b : rightBits) System.out.print(b + " ");
        System.out.println();
    }

    // ========================
    // MAIN (CASE STUDY TEST)
    // ========================
    public static void main(String[] args) {

        String S = "ACGTACGTACGTACGT";
        WaveletTreeDNA wt = new WaveletTreeDNA(S);

        System.out.println("Wavelet Tree Construction:");
        wt.display();

        int l = 3, r = 10; // S[3..10]
        int result = wt.rangeRankA(l, r);

        System.out.println("\nQuery: Count of 'A' in S[" + l + ".." + r + "]");
        System.out.println("Result: " + result);
    }
}

import java.util.Arrays;

public class LogisticsRadixSorter {

    /**
     * Core stable Counting Sort subroutine tailored to isolate and sort a single digit place.
     * @param array The target inventory array to sort.
     * @param exp The current exponent representing the digit place value (1, 10, 100, etc.)
     */
    private static void stableCountingSortByDigit(int[] array, int exp) {
        int n = array.length;
        int[] output = new int[n];
        int[] count = new int[10]; // Base-10 digits (0-9)
        
        Arrays.fill(count, 0);

        // 1. Accumulate frequencies of the specific digit place
        for (int i = 0; i < n; i++) {
            int digit = (array[i] / exp) % 10;
            count[digit]++;
        }

        // 2. Transform counts to prefix sums to determine actual output placement indexes
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // 3. Build the output array in REVERSE order to preserve stability
        for (int i = n - 1; i >= 0; i--) {
            int digit = (array[i] / exp) % 10;
            output[count[digit] - 1] = array[i];
            count[digit]--;
        }

        // 4. Transfer sorted state back into original array buffer
        System.arraycopy(output, 0, array, 0, n);
    }

    /**
     * Executes an LSD Radix Sort across the array entries.
     */
    public static void radixSort(int[] array) {
        if (array == null || array.length == 0) return;

        // Find the maximum value to determine how many digit positions exist
        int maxVal = array[0];
        for (int val : array) {
            if (val > maxVal) {
                maxVal = val;
            }
        }

        int passCounter = 1;
        // Loop through each digit place value (1s, 10s, 100s...)
        for (int exp = 1; maxVal / exp > 0; exp *= 10) {
            stableCountingSortByDigit(array, exp);
            System.out.println("State after Pass " + passCounter + " (Digit Exp " + exp + "): " + Arrays.toString(array));
            passCounter++;
        }
    }

    public static void main(String[] args) {
        int[] inventoryBatch = {329, 457, 657, 839, 436};

        System.out.println("Initial Package Inventory Array: " + Arrays.toString(inventoryBatch));
        System.out.println("----------------------------------------------------------------------");
        
        radixSort(inventoryBatch);
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Final Validated Sorted Package Array:   " + Arrays.toString(inventoryBatch));
    }
}

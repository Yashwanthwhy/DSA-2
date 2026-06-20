import java.util.Arrays;

public class InventoryOptimizer {

    /**
     * Solves the Unbounded Knapsack problem to maximize inventory profit.
     * @param weights Array containing the weights of each product category.
     * @param profits Array containing the profits of each product category.
     * @param capacity The maximum weight capacity allowed in the warehouse.
     * @return The absolute maximum profit achievable.
     */
    public static int maximizeInventoryValue(int[] weights, int[] profits, int capacity) {
        int n = weights.length;
        
        // dp[i] stores the max profit achievable for a sub-capacity of weight i
        int[] dp = new int[capacity + 1];
        
        // Base case initialization
        Arrays.fill(dp, 0);

        // Iterate through all sub-capacities from 1 up to the target capacity
        for (int w = 1; w <= capacity; w++) {
            for (int i = 0; i < n; i++) {
                if (weights[i] <= w) {
                    int alternativeProfit = profits[i] + dp[w - weights[i]];
                    if (alternativeProfit > dp[w]) {
                        dp[w] = alternativeProfit;
                    }
                }
            }
        }

        return dp[capacity];
    }

    public static void main(String[] args) {
        // Data points defined in the case study problem scenario
        int[] itemWeights = {2, 3, 4};
        int[] itemProfits = {30, 50, 60};
        int maxWarehouseCapacity = 8;

        System.out.println("Launching Supply Chain Optimization Routing System...");
        System.out.println("Available Item Categories: " + itemWeights.length);
        System.out.println("Target Warehouse Capacity Constraint: " + maxWarehouseCapacity + " kgn");

        int optimalProfit = maximizeInventoryValue(itemWeights, itemProfits, maxWarehouseCapacity);

        System.out.println("-------------------------------------------------------");
        System.out.println("Maximum Achievable Inventory Profit: " + optimalProfit);
        System.out.println("-------------------------------------------------------");
    }
}

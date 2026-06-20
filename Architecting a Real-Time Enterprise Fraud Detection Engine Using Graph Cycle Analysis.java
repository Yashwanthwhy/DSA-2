import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FraudDetectionEngine {

    // Definition of the states for Tri-Color Marking
    enum NodeColor {
        WHITE, // Unvisited
        GRAY,  // Active in current path / Under investigation
        BLACK  // Completely processed with no cycles
    }

    private final int totalAccounts;
    private final List<List<Integer>> adjacencyList;
    private final List<Integer> activePathTrack;
    private boolean cycleDetected = false;

    public FraudDetectionEngine(int accountsCount) {
        this.totalAccounts = accountsCount;
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < accountsCount; i++) {
            this.adjacencyList.add(new ArrayList<>());
        }
        this.activePathTrack = new ArrayList<>();
    }

    public void addTransaction(int fromAccount, int toAccount) {
        this.adjacencyList.get(fromAccount).add(toAccount);
    }

    /**
     * Top-level engine call to scan the entire transaction network for cycles.
     */
    public void detectFraudulentLoops() {
        NodeColor[] colors = new NodeColor[totalAccounts];
        for (int i = 0; i < totalAccounts; i++) {
            colors[i] = NodeColor.WHITE;
        }

        System.out.println("Beginning Real-Time Network Graph Analysis...");
        System.out.println("------------------------------------------------");

        for (int account = 0; account < totalAccounts; account++) {
            if (colors[account] == NodeColor.WHITE) {
                if (dfsColorTrace(account, colors)) {
                    cycleDetected = true;
                }
            }
        }

        if (!cycleDetected) {
            System.out.println("SUCCESS: Transaction network is verified clean. No loops found.");
        }
    }

    /**
     * Recursive Tri-Color DFS algorithm.
     */
    private boolean dfsColorTrace(int current, NodeColor[] colors) {
        // Push node onto the active tracking path
        colors[current] = NodeColor.GRAY;
        activePathTrack.add(current);

        for (int neighbor : adjacencyList.get(current)) {
            // Case 1: Hit a GRAY node - Fraudulent Cycle Confirmed
            if (colors[neighbor] == NodeColor.GRAY) {
                printFraudRingReport(neighbor, current);
                return true;
            }
            
            // Case 2: Hit an unvisited WHITE node - Traverse deeper
            if (colors[neighbor] == NodeColor.WHITE) {
                if (dfsColorTrace(neighbor, colors)) {
                    return true;
                }
            }
            // Case 3: BLACK nodes are skipped immediately as they are verified safe
        }

        // Backtrack: Pop node from path stack and finalize its state to BLACK
        activePathTrack.remove(activePathTrack.size() - 1);
        colors[current] = NodeColor.BLACK;
        return false;
    }

    /**
     * Extracts and prints the transaction loop path.
     */
    private void printFraudRingReport(int loopStartNode, int loopEndNode) {
        System.out.println("!!! SECURITY ALERT: FRAUDULENT CIRCULAR REVENUE RING DETECTED !!!");
        List<Integer> fraudCyclePath = new ArrayList<>();
        
        // Backtrack through active path trace to extract the loop sequence
        for (int i = activePathTrack.size() - 1; i >= 0; i--) {
            int account = activePathTrack.get(i);
            fraudCyclePath.add(account);
            if (account == loopStartNode) {
                break;
            }
        }
        Collections.reverse(fraudCyclePath);
        fraudCyclePath.add(loopStartNode); // Complete the loop visualization

        System.out.print("Identified Laundering Sequence Flow: ");
        for (int i = 0; i < fraudCyclePath.size(); i++) {
            System.out.print("Account [" + fraudCyclePath.get(i) + "]");
            if (i < fraudCyclePath.size() - 1) System.out.print(" -> ");
        }
        System.out.println("nAction Taken: Associated nodes flagged; outbound asset clearance frozen.");
    }

    public static void main(String[] args) {
        // Instantiate graph matching parameters in Question A
        int totalAccounts = 5; // Accounts 0, 1, 2, 3, 4
        FraudDetectionEngine engine = new FraudDetectionEngine(totalAccounts);

        // Populate system with active ledger routes
        engine.addTransaction(0, 1);
        engine.addTransaction(0, 2);
        engine.addTransaction(1, 2);
        engine.addTransaction(2, 3);
        engine.addTransaction(3, 4);
        engine.addTransaction(4, 2); // Creates loop back to Account 2

        engine.detectFraudulentLoops();
    }
}

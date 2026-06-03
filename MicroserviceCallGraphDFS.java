import java.util.*;

public class MicroserviceCallGraphDFS {

    private Map<String, List<String>> graph = new HashMap<>();
    private Set<String> visited = new HashSet<>();
    private List<String> entryOrder = new ArrayList<>();
    private List<String> finishOrder = new ArrayList<>();

    // Add directed edge
    public void addEdge(String source, String destination) {
        graph.computeIfAbsent(source, k -> new ArrayList<>()).add(destination);
        graph.computeIfAbsent(destination, k -> new ArrayList<>());
    }

    // DFS Traversal
    public void dfs(String service) {
        visited.add(service);
        entryOrder.add(service);

        List<String> neighbors = graph.get(service);
        Collections.sort(neighbors); // alphabetical order

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor);
            }
        }

        finishOrder.add(service);
    }

    public static void main(String[] args) {

        MicroserviceCallGraphDFS micro = new MicroserviceCallGraphDFS();

        // Create microservice call graph
        micro.addEdge("API", "auth");
        micro.addEdge("API", "catalogue");

        micro.addEdge("auth", "analytics");

        micro.addEdge("catalogue", "inventory");
        micro.addEdge("catalogue", "cart");

        micro.addEdge("cart", "payment");

        micro.addEdge("payment", "notify");
        micro.addEdge("payment", "ship");

        // Start DFS from API
        micro.dfs("API");

        // Display Entry Order
        System.out.println("Entry Order:");
        System.out.println(String.join(" -> ", micro.entryOrder));

        // Display Finish Order
        System.out.println("\nFinish Order:");
        System.out.println(String.join(" -> ", micro.finishOrder));

        // Display Downstream Services
        System.out.println("\nDownstream Services:");
        for (String service : micro.entryOrder) {
            if (!service.equals("API")) {
                System.out.print(service + " ");
            }
        }

        int V = micro.graph.size();

        int E = 0;
        for (List<String> list : micro.graph.values()) {
            E += list.size();
        }

        System.out.println("\n\nTime Complexity:");
        System.out.println("O(V + E) = O(" + V + " + " + E + ") = O(" + (V + E) + ")");
    }
}

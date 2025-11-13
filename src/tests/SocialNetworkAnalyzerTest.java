package tests;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import networkClasses.SocialNetworkAnalyzer;

public class SocialNetworkAnalyzerTest {
	
	public static void main(String[] args) {
		// Example test graph
        List<String> alice = Arrays.asList("Bob", "Charlie");
        List<String> bob   = Arrays.asList("Alice", "David");
        List<String> charlie = Arrays.asList("Bob", "Erin");
        List<String> david   = Arrays.asList("Alice");
        List<String> erin    = Arrays.asList();
        List<String> frank   = Arrays.asList("George");
        List<String> george  = Arrays.asList("Frank");

        Map<String, List<String>> graph = new HashMap<>();
        graph.put("Alice", alice);
        graph.put("Bob", bob);
        graph.put("Charlie", charlie);
        graph.put("David", david);
        graph.put("Erin", erin);
        graph.put("Frank", frank);
        graph.put("George", george);

        SocialNetworkAnalyzer sna = new SocialNetworkAnalyzer(graph);

        sna.printGraph();
        System.out.println("Cliques: " + sna.findCliques());
        System.out.println("Broadcast reach for Bob: " + sna.getBroadcastReach("Bob"));
        System.out.println("Influence range [1,3]: " + sna.getUsersInInfluenceRange(1, 3));
	}
}

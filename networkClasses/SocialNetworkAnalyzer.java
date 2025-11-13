package networkClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


public class SocialNetworkAnalyzer {
    /**
     * Constructor for the SocialNetworkAnalyzer. It takes the network data
     * and builds the internal graph representation.
     * @param followsGraph A map where the key is a user ID and the value is a list
     * of user IDs that the key user follows.
     * DONE IN ECLIPSE
     */
	
	private Map<String, Set<String>> graph;
	private Map<String, Set<String>> reverseGraph;
	
	// 1) Let's create the graph and a graph with reverse-pointed arrows
    public SocialNetworkAnalyzer(Map<String, List<String>> followsGraph) {
        // 1a) initialize the graphs
    	// graph are users who YOU follow, reverseGraph are your followERS!
    	graph = new HashMap<>();
    	reverseGraph = new HashMap<>();
    	
    	// 2a) Initialize the users
    	// 2b) from the followsGraph graph, each UNIQUE user will be added
    	// 2c) use putIfAbsent method
    	for(String user: followsGraph.keySet()) {
    		graph.putIfAbsent(user, new HashSet<>());
    		reverseGraph.putIfAbsent(user, new HashSet<>());

    		for (String following: followsGraph.get(user)) {
        		graph.putIfAbsent(following, new HashSet<>());
        		reverseGraph.putIfAbsent(following, new HashSet<>());
    		}
    	}
    	
    	// 3a) add edges for the graph, if they exist
    	
    	for(Map.Entry<String, List<String>> item : followsGraph.entrySet()) {
    		String user = item.getKey();
    		for(String following: item.getValue()) {
    			graph.get(user).add(following);
    			reverseGraph.get(following).add(user);

    		}
    		
    	}
    	
    	
    	
    }
    
    public void printGraph() {

    	System.out.println("Graph:");
    	for(String user: graph.keySet()) {
    		System.out.println(user + " -> " + graph.get(user));
    		
    	}
    	System.out.println();
    	
    	System.out.println("Reverse Graph:");
    	for(String user: reverseGraph.keySet()) {
    		System.out.println(user + " <- " + reverseGraph.get(user));
    	}
    	System.out.println();
    }
    


    /**
     * Finds all the cliques in the social network.
     * A clique is a group of 2 or more users who are all mutually connected.
     * @return A list of sets, where each set represents a clique of user IDs.
     * Idea: 
     */
    //use Kosuraju's Algorithm or any DFS 
    
    public List<Set<String>> findCliques() {
    	Stack<String> stack = new Stack<>(); //DFS requires a stack method
    	Set<String> visited = new HashSet<>(); //visited nodes are a set of strings
    	
    	// check graph and what nodes are connected
    	for(String user: graph.keySet()) {
    		if(visited.contains(user) == false) {
    			//DFS push
    			DFSPush(visited, user, stack);
    		}
    	}
    	
    	// check reverseGraph to see which ones connect back (SCC)
    	visited.clear();
    	List<Set<String>> cliques = new ArrayList<>();
    	
    	while(stack.isEmpty() == false) {
    		String user = stack.pop();
    		if(visited.contains(user) == false) {
    			Set<String> clique = DFSPop(visited, user, reverseGraph);
    			if(clique.size() > 1) {
    				cliques.add(clique);
    			}
    		}
    	}
    	return cliques;
    }
    
    private void DFSPush (Set<String> visited, String user, Stack<String> stack) {
    	visited.add(user);
    	for(String neighbor: graph.get(user)) {
    		if(visited.contains(neighbor) == false) {
    			DFSPush(visited, neighbor, stack);	
    		}
    	}
    	stack.push(user);	
    }
    
    private Set<String> DFSPop(Set<String> visited, String user, Map<String, Set<String>> reverseGraph) {
        Set<String> clique = new HashSet<>();
        Stack<String> dfs = new Stack<>();
        dfs.push(user);

        while (!dfs.isEmpty()) {
            String curr = dfs.pop();
            if (!visited.contains(curr)) {
                visited.add(curr);
                clique.add(curr);

                for (String neighbor : reverseGraph.get(curr)) {
                    if (!visited.contains(neighbor)) {
                        dfs.push(neighbor);
                    }
                }
            }
        }

        return clique;
    }
    private void printCliques(List<Set<String>> cliques) {
    	Map<String, Set<String>> cliqueSCC = new HashMap<>(); 
    	System.out.println("Cliques: " + cliques.size());
    	int i = 1;
    	for(Set<String> clique: cliques) {
    			System.out.println(clique);	
    	}

    }
    

 
    //create a new HashTable mutualGraph, which will store each user CANCELLED
    //1) for each user key, check its neighbors.
    //2) if the user contains neighbor and vice versa, it is a mutual
    //2a) if true, add to the mutualGraph.get(user)
    //2b) else, create a new HashSet and put the neighbor there


    /**
     * Calculates the "broadcast reach" of a user's post.
     * This includes all users who follow the given user, directly or indirectly.
     * @param userId The ID of the user whose post reach is being calculated.
     * @return A set of user IDs representing the total audience.
     */
    //gets parameter of user id
    //we will do a BFS SEARCH! create a set of objects to queue, which will check neighbors
    //all completed elements will be put in the check hashset, which removes from the queue
    public Set<String> getBroadcastReach(String userId) {
        // Your implementation here
    	Set<String> checked = new HashSet<>();
    	Queue<String> queue = new LinkedList<>();
    	
    	checked.add(userId);
    	queue.add(userId);
    	
    	while(!queue.isEmpty()) {
    		String current = queue.poll();
    			for(String neighbor: graph.get(current)) {
    				if (!checked.contains(neighbor)) {
    					checked.add(neighbor);
    					queue.add(neighbor);
    				}
    			}
    	}
    	return checked;
    }


    /**
     * Finds all users whose follower count is within a specified range.
     * @param minFollowers The minimum follower count (inclusive).
     * @param maxFollowers The maximum follower count (inclusive).
     * Idea: Create a Binary Search Tree; this will grab all users from the minFollowers
     * to the maxFollowers of the tree. 
     * @return A sorted list of user IDs that fall within the influence range.
     */

    public List<String> getUsersInInfluenceRange(int minFollowers, int maxFollowers) {
        // Your implementation here
    	Map<String, Integer> followerSet = getFollowerSet(graph);
    	//create a binary search tree
    	//for each user in followerSet.keySet, use in-order traversal
    	
      	System.out.println("\nCreate new Binary Search Tree:\n");
      	//create the binary search tree object, which will be used for
      	//creating searching the elements in between
    	BST userTree = new BST();
    	for(String user: graph.keySet()) {
    		userTree.insert(user, followerSet.get(user));
    	}
    	
    	//create a list containing all the elements found in the influence range
    	//the influence range is done in the private class BST, which will search recursively
    	//for numbers within
    	List<String> result = new ArrayList<>(); 
    	userTree.inOrderInfluenceRange(userTree.root, minFollowers, maxFollowers, result);
    	return result;
    }
    
    //we need to find the set of followers for each user
    private Map<String, Integer> getFollowerSet(Map<String, Set<String>> graph) {
    	Map<String, Integer> users = new HashMap<>();
    	for(String user: graph.keySet()) {
    		System.out.println("User " + user + "'s follower count:" + graph.get(user).size());
    		users.put(user, graph.get(user).size()); //find a way to count followers in each 
    	}
    	
    	
    	return users;
    }

    private static class Node {
    	String name;
    	int followers;
    	Node left;
    	Node right;
    	
    	Node(String name, int followers) {
    		this.name = name;
    		this.followers = followers;
    		this.left = null;
    		this.right = null;
    	}
    }
    //create the BSt
    private class BST {
        Node root;

        BST() {
            root = null;
        }
        //this inserts the node, which first enters a recursive method
        void insert(String name, int key) {
            root = insert(root, name, key);
        }
        //recursive method will check where the current node can be placed
        private Node insert(Node root, String name, int key) {
            if (root == null) {
            	return new Node(name, key);
            }
            //go left of the tree if less than current node value
            if (key < root.followers) {
                root.left = insert(root.left, name, key);
            }
            //go right if greater 
            else if (key > root.followers) {
                root.right = insert(root.right, name, key);
            }
            //also go right if equal
            else {
            	root.right = insert(root.right, name, key);
            }
            return root;
        }

        void inOrderInfluenceRange(Node root, int min, int max, List<String> result) {
        	//check if null
            if (root == null) {
            	return;
            }
            //if greater than the min, time to do recursive
            if (root.followers > min) {
            	inOrderInfluenceRange(root.left, min, max, result);
            }
            //base case
            if (root.followers >= min && root.followers <= max) {
            	result.add(root.name);
            } //if greater than max, time to do recursive
            if (root.followers < max) {
            	inOrderInfluenceRange(root.right, min, max, result);
            }
        }
    }
    

}

# SocialNetworkAnalyzerDSA
Creates a graph of users in a social media app. Methods related to the graph are finding cliques, finding the broadcast reach of a user, and a list of users in a range of followers.

---

## 🔁 Key Features

### 1. Broadcast Reach
> Given a starting user, find all users reachable by following outgoing edges.

- Implemented with **BFS**  
- Uses a queue and a `visited` set  
- Avoids revisiting nodes and infinite loops

### 2. Strongly Connected Components (SCCs)
> Identify tightly connected groups where each user is reachable from every other user in the group.

Implementation (e.g., Kosaraju’s algorithm):

1. Run DFS on the original graph, pushing nodes to a stack by finish time  
2. Reverse all edges in the graph  
3. Pop nodes from the stack and DFS on the reversed graph  
4. Each DFS tree in the reversed graph gives one SCC

### 3. Influence Range (by follower count)
> Filter users based on how many followers they have.

- Compute follower counts from the adjacency structure  
- Return users whose follower count falls within `[minFollowers, maxFollowers]`

---

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/JCSkies/SocialNetworkAnalyzer.git
cd SocialNetworkAnalyzer

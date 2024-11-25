package com.example.specialprojectreal;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Graph_dq {
    public int[] dist;
    int previous[];
    Set<Integer> visited;
    PriorityQueue<Node> pqueue;
    int V; // Number of vertices
    List<List<Node>> adj_list;
    int[] previousNodes;

    //class constructor
    public Graph_dq(int V) {
        this.V = V;
        dist = new int[V];
        previous = new int[V];
        visited = new HashSet<Integer>();
        pqueue = new PriorityQueue<Node>(V, new Node());
    }

    // Dijkstra's Algorithm implementation
    public void algo_dijkstra(List<List<Node>> adj_list, int src_vertex, Node src_node) {
        this.adj_list = adj_list;

        for (int i = 0; i < V; i++)
            dist[i] = Integer.MAX_VALUE;

        // first add source vertex to PriorityQueue
        pqueue.add(src_node);

        // Distance to the source from itself is 0
        dist[src_vertex] = 0;
        while (visited.size() != V) {

            // u is removed from PriorityQueue and has min distance
            int u = pqueue.remove().node;

            // add node to finalized list (visited)
            visited.add(u);
            graph_adjacentNodes(u);
        }
    }

    private void setPreviousNodes(int u, int i) {

        Node currNode = new Node(adj_list.get(u).get(i).node, 0);
        int listIndex = 0;
        int nodeIndex = 0;
        for (int p = 0; p < adj_list.size(); p++) { //Getting Previous Node List Index and Node Index
            Node v = new Node(u, 0); //If  u was 2 finds the first occurrence of a node with an int of 2
            int inde = adj_list.get(p).indexOf(v); //Finds the first occurrence of V Node
            if (inde != -1) { //If Not found
                listIndex = p;
                nodeIndex = inde;
                break;
            }
        }
        for (List<Node> nodes : adj_list) { //Finds all currNodes and changes List Index and Node Index to previous Node;
            int inde2 = nodes.indexOf(currNode);
            if (inde2 != -1) {
                nodes.get(inde2).setListIndex(listIndex);
                nodes.get(inde2).setNodeIndex(nodeIndex);
            }
        }

    }

    // this methods processes all neighbours of the just visited node
    private void graph_adjacentNodes(int u) {
        int edgeDistance = -1;
        int newDistance = -1;

        // process all neighbouring nodes of u
        for (int i = 0; i < adj_list.get(u).size(); i++) {
            Node v = adj_list.get(u).get(i);

            //  proceed only if current node is not in 'visited'
            if (!visited.contains(v.node)) {
                edgeDistance = v.cost;
                newDistance = dist[u] + edgeDistance;

                // compare distances
                if (newDistance < dist[v.node]) {
                    dist[v.node] = newDistance;
                    setPreviousNodes(u, i);
                }


                // Add the current vertex to the PriorityQueue
                pqueue.add(new Node(v.node, dist[v.node]));
            }

        }
    }
}

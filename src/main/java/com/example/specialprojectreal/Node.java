package com.example.specialprojectreal;

import java.util.Comparator;

public class Node implements Comparator<Node> {
    public int node;
    public int cost;
    public int listIndex = -1;
    public int nodeIndex = -1;
    public String name;


    public Node() {
    } //empty constructor

    public Node(int node, int cost,String name) {
        this.node = node;
        this.cost = cost;
        this.name = name;
    }

    public Node(int node, int cost) {
        this.node = node;
        this.cost = cost;
    }

    public int getNode() {
        return node;
    }

    public String getName(){
        return name;
    }


    @Override
    public int compare(Node node1, Node node2) {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object p) { //Overriding Equals Method
        return (this.node == ((Node) p).node);
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public int getListIndex() {
        return listIndex;
    }
}

package org.example;

public class Grid {
    private int numberOfNodes;
    private int numberOfElements;
    private Node[] nodes;
    private Element[] elements;

    public Grid() {
    }

    public Grid(int numberOfNodes, int numberOfElements, Node[] nodes, Element[] elements) {
        this.numberOfNodes = numberOfNodes;
        this.numberOfElements = numberOfElements;
        this.nodes = nodes;
        this.elements = elements;
    }

    @Override
    public String toString() {
        String str = "Grid{" + "\n" + "Number of nodes: " + numberOfNodes + "\n" + "Number od elements: " + numberOfElements + "\n";
        str += "Nodes:" + "\n";
        for(int i = 0; i < numberOfNodes; i++){

            str += i + ". (" + nodes[i] + ")" + "\n";
        }
        str += "Elements:" + "\n";
        for(int i = 0; i < numberOfElements; i++){

            str += i + ". (" + elements[i] + ")" + "\n";
        }
        return str + "}";
    }
}

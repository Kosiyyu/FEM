package org.example;

import lombok.Data;

@Data
public class Node {

    private int nodeId;
    private double x = 0.0;
    private double y = 0.0;
    private int BC = 0;

    public Node() {
    }

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Node(int nodeId, double x, double y) {
        this.nodeId = nodeId;
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "x: " + this.x + " , y: " + this.y + " , BC: " + this.BC;
    }
}
package org.example;

import lombok.Data;

@Data
public class Element {

    private int elementId;
    private int[] nodeIds;

    private double[][] H;
    private double[][] HBC;

    public Element(int elementId, int[] ID) {
        this.elementId = elementId;
        this.nodeIds = ID;
    }

    public String toString() {
        return "ID[0]: " + nodeIds[0] + ", ID[1]: " + nodeIds[1] + ", ID[2]: " + nodeIds[2] + ", ID[3]: " + nodeIds[3];
    }
}
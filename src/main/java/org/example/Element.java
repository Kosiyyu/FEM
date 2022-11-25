package org.example;

import lombok.Data;

@Data
public class Element {

    private int elementId = -1;
    private int[] ID = new int[4];

    public Element() {
    }

    public Element(int elementId, int[] ID) {
        this.elementId = elementId;
        this.ID = ID;
    }

    public String toString() {
        return "ID[0]: " + ID[0] + ", ID[1]: " + ID[1] + ", ID[2]: " + ID[2] + ", ID[3]: " + ID[3];
    }
}
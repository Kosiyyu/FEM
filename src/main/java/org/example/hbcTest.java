package org.example;

public class hbcTest {
    public static void main(String[] args) {
        Node[] nodes = {new Node(0., 0.), new Node(0.02,0.), new Node(0.07, 0.04), new Node(0.,0.02)};//fejke nodey
        for (int i = 0; i < 4; i++){//petla po kazdej scianei elementu
            double detJ;
            if(i != 3){
                detJ = MathFunctions.distance(nodes[i], nodes[i + 1]);
            }
            else {
                detJ = MathFunctions.distance(nodes[3], nodes[0]);
            }
        }
    }
}

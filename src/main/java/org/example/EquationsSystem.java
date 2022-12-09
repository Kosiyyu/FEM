package org.example;

import lombok.Data;

@Data
public class EquationsSystem {

    private double[][] HG;

    private int size;
    public EquationsSystem(int size) {
        this.HG = new double[size][size];
    }

    public void addH(Element e){
        int []nodeIDs = e.getNodeIds();
        double [][] H = e.getH();

        for(int i = 0; i < H.length; i++){
            for (int j = 0; j < H[0].length; j++){
                //System.out.println(nodeIDs[i] - 1 + " " + (nodeIDs[j] - 1));
                HG[nodeIDs[i] - 1][nodeIDs[j] - 1] += H[i][j];
            }
        }
    }

    public void addHBC(Element e){
        int []nodeIDs = e.getNodeIds();
        double [][] HBC = e.getHBC();

        for(int i = 0; i < HBC.length; i++){
            for (int j = 0; j < HBC[0].length; j++){
                //System.out.println(nodeIDs[i] - 1 + " " + (nodeIDs[j] - 1));
                HG[nodeIDs[i] - 1][nodeIDs[j] - 1] += HBC[i][j];
            }
        }
    }
}

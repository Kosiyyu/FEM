package org.example;

import lombok.Data;

@Data
public class EquationsSystem {

    private double[][] HG;

    private double[][] CG;

    private double[][] PG;

    private int size;
    public EquationsSystem(int size) {
        this.HG = new double[size][size];
        this.CG = new double[size][size];
        this.PG = new double[size][1];
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

    public void addC(Element e){
        int []nodeIDs = e.getNodeIds();
        double [][] C = e.getC();

        for(int i = 0; i < C.length; i++){
            for (int j = 0; j < C[0].length; j++){
                //System.out.println(nodeIDs[i] - 1 + " " + (nodeIDs[j] - 1));
                CG[nodeIDs[i] - 1][nodeIDs[j] - 1] += C[i][j];
            }
        }
    }

    public void addP(Element e) {
        int []nodeIDs = e.getNodeIds();
        double [][] P = e.getP();
        for(int i = 0; i < P.length; i++){
            for (int j = 0; j < P[0].length; j++){
                //System.out.println(j);
                //System.out.println(nodeIDs[i] - 1 + " " + (nodeIDs[j] - 1));
                PG[nodeIDs[i] - 1][j] += P[i][j];
            }
        }
    }
}

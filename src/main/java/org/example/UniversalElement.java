package org.example;

import lombok.Data;

@Data
public class UniversalElement {

    private double[][] deltaNOverDeltaKsi;

    private double[][] deltaNOverDeltaEta;

    private double[][] N;

    private double[] pointsX;

    private double[] pointsY;

    private double[] weightsX;

    private double[] weightsY;

    public UniversalElement(double[] nodes, double[] weights, int nDSF) {
        deltaNOverDeltaKsi = new double[nodes.length * nodes.length][nDSF];
        deltaNOverDeltaEta = new double[nodes.length * nodes.length][nDSF];
        N = new double[nodes.length * nodes.length][nDSF];

        pointsX = new double[nodes.length * nodes.length];
        pointsY = new double[nodes.length * nodes.length];
        weightsX = new double[nodes.length * nodes.length];
        weightsY = new double[nodes.length * nodes.length];

        int counter = 0;
        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nodes.length; j++){
                pointsX[counter] = nodes[i];
                pointsY[counter] = nodes[j];
                weightsX[counter] = weights[i];
                weightsY[counter] = weights[j];
                counter++;
            }
        }


        for(int i = 0; i < nodes.length * nodes.length; i ++){
            for(int j = 0; j < nDSF; j ++){
                if(j == 0){
                    deltaNOverDeltaKsi[i][j] = -0.25 * (1 - pointsX[i]);
                    deltaNOverDeltaEta[i][j] = -0.25 * (1 - pointsY[i]);
                    N[i][j] = 0.25 * (1 - pointsY[i]) * (1 - pointsX[i]);
                }
                else if(j == 1){
                    deltaNOverDeltaKsi[i][j] = 0.25 * (1 - pointsX[i]);
                    deltaNOverDeltaEta[i][j] = -0.25 * (1 + pointsY[i]);
                    N[i][j] = 0.25 * (1 + pointsY[i]) * (1 - pointsX[i]);
                }
                else if(j == 2){
                    deltaNOverDeltaKsi[i][j] = 0.25 * (1 + pointsX[i]);
                    deltaNOverDeltaEta[i][j] = 0.25 * (1 + pointsY[i]);
                    N[i][j] = 0.25 * (1 + pointsY[i]) * (1 + pointsX[i]);
                }
                else if(j == 3){
                    deltaNOverDeltaKsi[i][j] = -0.25 * (1 + pointsX[i]);
                    deltaNOverDeltaEta[i][j] = 0.25 * (1 - pointsY[i]);
                    N[i][j] = 0.25 * (1 - pointsY[i]) * (1 + pointsX[i]);
                }
            }
        }

        //deltaNOverDeltaKsi = Matrix.transformVertically2dArray(getDeltaNOverDeltaKsi());
        //deltaNOverDeltaEta = Matrix.transformVertically2dArray(getDeltaNOverDeltaEta());
        //N = Matrix.transformVertically2dArray(getN());
    }





}

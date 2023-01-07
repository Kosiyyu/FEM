package org.example;

import lombok.Data;

@Data
public class UniversalElement {

    private double[][] deltaNOverDeltaKsi;

    private double[][] deltaNOverDeltaEta;

    private double[][] N;

    private double[] ksi;

    private double[] eta;

    private double[] weightsX;

    private double[] weightsY;

    public UniversalElement(double[] nodes, double[] weights, int nDSF) {
        deltaNOverDeltaKsi = new double[nodes.length * nodes.length][nDSF];
        deltaNOverDeltaEta = new double[nodes.length * nodes.length][nDSF];
        N = new double[nodes.length * nodes.length][nDSF];

        eta = new double[nodes.length * nodes.length];
        ksi = new double[nodes.length * nodes.length];
        weightsX = new double[nodes.length * nodes.length];
        weightsY = new double[nodes.length * nodes.length];

        int counter = 0;
        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nodes.length; j++){
                eta[counter] = nodes[i];
                ksi[counter] = nodes[j];
                weightsX[counter] = weights[i];
                weightsY[counter] = weights[j];
                counter++;
            }
        }


        for(int i = 0; i < nodes.length * nodes.length; i ++){
            for(int j = 0; j < nDSF; j ++){
                if(j == 0){
                    deltaNOverDeltaKsi[i][j] = -0.25 * (1 - eta[i]);
                    deltaNOverDeltaEta[i][j] = -0.25 * (1 - ksi[i]);
                    N[i][j] = 0.25 * (1 - ksi[i]) * (1 - eta[i]);
                }
                else if(j == 1){
                    deltaNOverDeltaKsi[i][j] = 0.25 * (1 - eta[i]);
                    deltaNOverDeltaEta[i][j] = -0.25 * (1 + ksi[i]);
                    N[i][j] = 0.25 * (1 + ksi[i]) * (1 - eta[i]);
                }
                else if(j == 2){
                    deltaNOverDeltaKsi[i][j] = 0.25 * (1 + eta[i]);
                    deltaNOverDeltaEta[i][j] = 0.25 * (1 + ksi[i]);
                    N[i][j] = 0.25 * (1 + ksi[i]) * (1 + eta[i]);
                }
                else if(j == 3){
                    deltaNOverDeltaKsi[i][j] = -0.25 * (1 + eta[i]);
                    deltaNOverDeltaEta[i][j] = 0.25 * (1 - ksi[i]);
                    N[i][j] = 0.25 * (1 - ksi[i]) * (1 + eta[i]);
                }
            }
        }
    }

    public static double [][] nForHbcAndP(double[] ksi, double[] eta, int nDSF){
        double [][] N = new double[ksi.length][eta.length];
        for(int i = 0; i < N.length; i++){
            for(int j = 0; j < nDSF; j ++){
                if(j == 0){
                    N[i][j] = 0.25 * (1 - ksi[i]) * (1 - eta[i]);
                }
                else if(j == 1){
                    N[i][j] = 0.25 * (1 + ksi[i]) * (1 - eta[i]);
                }
                else if(j == 2){
                    N[i][j] = 0.25 * (1 + ksi[i]) * (1 + eta[i]);
                }
                else if(j == 3){
                    N[i][j] = 0.25 * (1 - ksi[i]) * (1 + eta[i]);
                }
            }
        }
        return N;
    }



}

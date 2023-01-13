package org.example;

import lombok.Data;

@Data
public class UniversalElement {

    private double[][] dNBydKsi;

    private double[][] dNBydEta;

    private double[][] n;

    private double[] ksi;

    private double[] eta;

    private double[] ksiForHbcAndP;

    private double[] etaForHbcAndP;

    private double[] weightsX;

    private double[] weightsY;


    public UniversalElement(double[] nodes, double[] nodesForHbcAndP, double[] weights, int nDSF, int numberOfIntegrationPointsOnSide) {
        dNBydKsi = new double[nodes.length * nodes.length][nDSF];
        dNBydEta = new double[nodes.length * nodes.length][nDSF];

        n = new double[nodes.length * nodes.length][nDSF];

        eta = new double[nodes.length * nodes.length];
        ksi = new double[nodes.length * nodes.length];

        ksiForHbcAndP = new double[nodesForHbcAndP.length * 4];
        etaForHbcAndP = new double[nodesForHbcAndP.length * 4];

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
                    dNBydKsi[i][j] = -0.25 * (1 - eta[i]);
                    dNBydEta[i][j] = -0.25 * (1 - ksi[i]);
                    n[i][j] = 0.25 * (1 - ksi[i]) * (1 - eta[i]);
                }
                else if(j == 1){
                    dNBydKsi[i][j] = 0.25 * (1 - eta[i]);
                    dNBydEta[i][j] = -0.25 * (1 + ksi[i]);
                    n[i][j] = 0.25 * (1 + ksi[i]) * (1 - eta[i]);
                }
                else if(j == 2){
                    dNBydKsi[i][j] = 0.25 * (1 + eta[i]);
                    dNBydEta[i][j] = 0.25 * (1 + ksi[i]);
                    n[i][j] = 0.25 * (1 + ksi[i]) * (1 + eta[i]);
                }
                else if(j == 3){
                    dNBydKsi[i][j] = -0.25 * (1 + eta[i]);
                    dNBydEta[i][j] = 0.25 * (1 - ksi[i]);
                    n[i][j] = 0.25 * (1 - ksi[i]) * (1 + eta[i]);
                }
            }
        }

        counter = 0;
        //bok dol
        for (int i = 0; i < nodesForHbcAndP.length; i++) {//lece po jednym boku
            ksiForHbcAndP[counter] = nodesForHbcAndP[i];
            etaForHbcAndP[counter] = -1;
            counter++;
        }
        //bok prawo
        for (int i = 0; i < nodesForHbcAndP.length; i++) {//lece po jednym boku
            ksiForHbcAndP[counter] = 1;
            etaForHbcAndP[counter] = nodesForHbcAndP[i];
            counter++;
        }
        //bok gora
        for (int i = 0; i < nodesForHbcAndP.length; i++) {//lece po jednym boku
            ksiForHbcAndP[counter] = nodesForHbcAndP[i];
            etaForHbcAndP[counter] = 1;
            counter++;
        }
        //bok lewo
        for (int i = 0; i < nodesForHbcAndP.length; i++) {//lece po jednym boku
            ksiForHbcAndP[counter] = -1;
            etaForHbcAndP[counter] = nodesForHbcAndP[i];
            counter++;
        }
    }

    public static double [][] nForHbcAndP(int i, double [][] N, double[] pointsX, double[] pointsY, int nDSF, int x, int numberOfIntegrationPointsOnSide){
        double ksi = pointsX[numberOfIntegrationPointsOnSide * x + i];
        double eta = pointsY[numberOfIntegrationPointsOnSide * x + i];
        for (int j = 0; j < nDSF; j++) {
            if (j == 0) {
                N[i][j] = 0.25 * (1 - ksi) * (1 - eta);
            } else if (j == 1) {
                N[i][j] = 0.25 * (1 + ksi) * (1 - eta);
            } else if (j == 2) {
                N[i][j] = 0.25 * (1 + ksi) * (1 + eta);
            } else if (j == 3) {
                N[i][j] = 0.25 * (1 - ksi) * (1 + eta);
            }
        }
        return N;
    }

    public static double[] ksiForHbcAndP(int numberOfIntegrationPointsOnSide){
        double [] nodes2 = Functions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide);

        double[] pointsX = new double[numberOfIntegrationPointsOnSide * 4];
        double[] pointsY = new double[numberOfIntegrationPointsOnSide * 4];

        int counter = 0;
        //bok dol
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            pointsX[counter] = nodes2[i];
            pointsY[counter] = -1;
            counter++;
        }
        //bok prawo
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            pointsX[counter] = 1;
            pointsY[counter] = nodes2[i];
            counter++;
        }
        //bok gora
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            pointsX[counter] = nodes2[i];
            pointsY[counter] = 1;
            counter++;
        }
        //bok lewo
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            pointsX[counter] = -1;
            pointsY[counter] = nodes2[i];
            counter++;
        }
        return pointsX;
    }

    public static double [] etaForHbcAndP(int numberOfIntegrationPointsOnSide){
        double [] nodes2 = Functions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide);

        double[] pointsX = new double[numberOfIntegrationPointsOnSide * 4];
        double[] pointsY = new double[numberOfIntegrationPointsOnSide * 4];

        int counter = 0;
        //bok dol
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            pointsX[counter] = nodes2[i];
            pointsY[counter] = -1;
            counter++;
        }
        //bok prawo
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            pointsX[counter] = 1;
            pointsY[counter] = nodes2[i];
            counter++;
        }
        //bok gora
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            pointsX[counter] = nodes2[i];
            pointsY[counter] = 1;
            counter++;
        }
        //bok lewo
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            pointsX[counter] = -1;
            pointsY[counter] = nodes2[i];
            counter++;
        }
        return pointsX;
    }

}

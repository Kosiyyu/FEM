package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UniversalElement {

    private double[][] nOverKsi;

    private double[][] nOverEta;

    private double[][] nShapeValue;

    private double[] pointsX;

    private double[] pointsY;

    private double[] weightsX;

    private double[] weightsY;

    public UniversalElement(Point[] points, int nDSF) {
        this.nOverKsi = new double[points.length][nDSF];
        this.nOverEta = new double[points.length][nDSF];
        System.out.println("start:" + points.length);

        for(int i = 0; i < points.length; i ++){
            for(int j = 0; j < nDSF; j ++){
                if(j == 0){
                    nOverKsi[i][j] = -0.25 * (1 - points[i].x);
                    nOverEta[i][j] = -0.25 * (1 - points[i].y);
                }
                else if(j == 1){
                    nOverKsi[i][j] = 0.25 * (1 - points[i].x);
                    nOverEta[i][j] = -0.25 * (1 + points[i].y);
                }
                else if(j == 2){
                    nOverKsi[i][j] = 0.25 * (1 + points[i].x);
                    nOverEta[i][j] = 0.25 * (1 + points[i].y);
                }
                else if(j == 3){
                    nOverKsi[i][j] = -0.25 * (1 + points[i].x);
                    nOverEta[i][j] = 0.25 * (1 - points[i].y);
                }
            }
        }
    }

    public UniversalElement(double[] nodes, double[] weights, int nDSF) {
        nOverKsi = new double[nodes.length * nodes.length][nDSF];
        nOverEta = new double[nodes.length * nodes.length][nDSF];
        nShapeValue = new double[nodes.length * nodes.length][nDSF];

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
                    nOverKsi[i][j] = -0.25 * (1 - pointsX[i]);
                    nOverEta[i][j] = -0.25 * (1 - pointsY[i]);
                    nShapeValue[i][j] = 0.25 * (1 - pointsX[i]) * (1 - pointsY[i]);
                }
                else if(j == 1){
                    nOverKsi[i][j] = 0.25 * (1 - pointsX[i]);
                    nOverEta[i][j] = -0.25 * (1 + pointsY[i]);
                    nShapeValue[i][j] = 0.25 * (1 + pointsX[i]) * (1 - pointsY[i]);
                }
                else if(j == 2){
                    nOverKsi[i][j] = 0.25 * (1 + pointsX[i]);
                    nOverEta[i][j] = 0.25 * (1 + pointsY[i]);
                    nShapeValue[i][j] = 0.25 * (1 + pointsX[i]) * (1 + pointsY[i]);
                }
                else if(j == 3){
                    nOverKsi[i][j] = -0.25 * (1 + pointsX[i]);
                    nOverEta[i][j] = 0.25 * (1 - pointsY[i]);
                    nShapeValue[i][j] = 0.25 * (1 - pointsX[i]) * (1 + pointsY[i]);
                }
            }
        }
    }





}

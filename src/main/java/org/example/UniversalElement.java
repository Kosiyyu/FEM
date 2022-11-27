package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UniversalElement {

    private double[][] nOverKsi;
    private double[][] nOverEta;

    public UniversalElement(Point[] points, int nDSF) {
        this.nOverKsi = new double[points.length][nDSF];
        this.nOverEta = new double[points.length][nDSF];

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






}

package org.example;

import java.util.ArrayList;
import java.util.List;

public class Functions {

    public static double distance(Node p1, Node p2){
        return Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));
    }

    public static double[] nodesOfGaussianLagrangeQuadrature(int numberOfIntegrationPoints ){
        List<Double> nodes = new ArrayList<>();
        double [] nodesArray = new double[numberOfIntegrationPoints];

        if (numberOfIntegrationPoints == 2) {
            nodes.add(-1 / Math.sqrt(3));
            nodes.add(1 / Math.sqrt(3));
            for(int i = 0; i < nodes.size(); i++){
                nodesArray[i] = nodes.get(i);
            }
        } else if (numberOfIntegrationPoints == 3) {

            nodes.add(-Math.sqrt(3.0 / 5.0));
            nodes.add(0.0);
            nodes.add(Math.sqrt(3.0 / 5.0));
            for(int i = 0; i < nodes.size(); i++){
                nodesArray[i] = nodes.get(i);
            }
        } else if (numberOfIntegrationPoints == 4) {
//            nodes.add(-0.861136);
//            nodes.add(-0.339981);
//            nodes.add(0.339981);
//            nodes.add(0.861136);

            nodes.add(-Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))));
            nodes.add(-Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))));
            nodes.add(Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))));
            nodes.add(Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0))));
            for(int i = 0; i < nodes.size(); i++){
                nodesArray[i] = nodes.get(i);
            }
        } else if (numberOfIntegrationPoints == 5) {
//            nodes.add(-0.906180);
//            nodes.add(-0.538469);
//            nodes.add(0.0);
//            nodes.add(0.538469);
//            nodes.add(0.906180);

            nodes.add(-(1.0 / 3.0) * Math.sqrt(5.0 + (2.0 * Math.sqrt(10.0 / 7.0))));
            nodes.add(-(1.0 / 3.0) * Math.sqrt(5.0 - (2.0 * Math.sqrt(10.0 / 7.0))));
            nodes.add(0.0);
            nodes.add((1.0 / 3.0) * Math.sqrt(5.0 - (2.0 * Math.sqrt(10.0 / 7.0))));
            nodes.add((1.0 / 3.0) * Math.sqrt(5.0 + (2.0 * Math.sqrt(10.0 / 7.0))));

            for(int i = 0; i < nodes.size(); i++){
                nodesArray[i] = nodes.get(i);
            }
        } else {
            nodes = null;
            System.out.println("Invalid n");
        }
        return nodesArray;
    }

    public static double[] weightsOfGaussianLagrangeQuadrature(int numberOfIntegrationPoints ) {
        List<Double> weights = new ArrayList<>();
        double [] weightsArray = new double[numberOfIntegrationPoints];
        if (numberOfIntegrationPoints == 2) {
            weights.add(1.0);
            weights.add(1.0);
            for(int i = 0; i < weights.size(); i++){
                weightsArray[i] = weights.get(i);
            }
        } else if (numberOfIntegrationPoints == 3) {
            weights.add(5.0 / 9.0);
            weights.add(8.0 / 9.0);
            weights.add(5.0 / 9.0);
            for(int i = 0; i < weights.size(); i++){
                weightsArray[i] = weights.get(i);
            }
        } else if (numberOfIntegrationPoints == 4) {
//            weights.add(0.347855);
//            weights.add(0.652145);
//            weights.add(0.652145);
//            weights.add(0.347855);

            weights.add((18.0 - Math.sqrt(30.0)) / 36.0);
            weights.add((18.0 + Math.sqrt(30.0)) / 36.0);
            weights.add((18.0 + Math.sqrt(30.0)) / 36.0);
            weights.add((18.0 - Math.sqrt(30.0)) / 36.0);
            for(int i = 0; i < weights.size(); i++){
                weightsArray[i] = weights.get(i);
            }
        } else if (numberOfIntegrationPoints == 5) {
//            weights.add(0.236927);
//            weights.add(0.478629);
//            weights.add(0.568889);
//            weights.add(0.478629);
//            weights.add(0.236927);

            weights.add((322.0 - (13.0 * Math.sqrt(70))) / 900.0);
            weights.add((322.0 + (13.0 * Math.sqrt(70))) / 900.0);
            weights.add(128.0 / 225.0);
            weights.add((322.0 + (13.0 * Math.sqrt(70))) / 900.0);
            weights.add((322.0 - (13.0 * Math.sqrt(70))) / 900.0);
            for(int i = 0; i < weights.size(); i++){
                weightsArray[i] = weights.get(i);
            }
        } else {
            weights = null;
            System.out.println("Invalid n");
        }
        return weightsArray;
    }


}

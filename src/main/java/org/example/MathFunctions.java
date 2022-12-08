package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MathFunctions {

    @Data
    public static class NodesAndCoefficients{
        private Point[] nodePoints = null;
        private Point[] coefficientPoints = null;

        public NodesAndCoefficients(Point[] nodePoints, Point[] coefficientPoints) {
            this.nodePoints = nodePoints;
            this.coefficientPoints = coefficientPoints;
        }
    }

    public static double distance(Point p1, Point p2){
        return Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
    }

    public static double integerQuadrature(int N, double upperBoundary, double lowerBoundary, FunctionalInterface functionalInterface){
        double det = (upperBoundary - lowerBoundary) / 2;
        List<Double> nodes = new ArrayList<>();
        List<Double> weight = new ArrayList<>();
        if(N == 1){
            nodes.add(1 / Math.sqrt(3));weight.add(1.0);
            nodes.add(-1 / Math.sqrt(3));weight.add(1.0);
        }
        else if (N == 2) {
            nodes.add(Math.sqrt(3.0 / 5.0));weight.add(5.0 / 9.0);
            nodes.add(0.0);weight.add(8.0 / 9.0);
            nodes.add(-Math.sqrt(3.0 / 5.0));weight.add(5.0 / 9.0);
        }
        else if (N == 3) {
            nodes.add(-0.861136);weight.add(0.347855);
            nodes.add(-0.339981);weight.add(0.652145);
            nodes.add(0.339981);weight.add(0.652145);
            nodes.add(0.861136);weight.add(0.347855);
        }
        else if (N == 4) {
            nodes.add(-0.906180);weight.add(0.236927);
            nodes.add(-0.538469);weight.add(0.478629);
            nodes.add(0.0);weight.add(0.568889);
            nodes.add(0.538469);weight.add(0.478629);
            nodes.add(0.906180);weight.add(0.236927);
        }
        else {
            System.out.println("Invalid n");
            System.exit(0);
        }

        double sum = 0.0;
        for(int i = 0; i < nodes.size(); i++){
            nodes.set(i, (((1 - nodes.get(i)) / 2) * lowerBoundary) + (((nodes.get(i) + 1) / 2) * upperBoundary));
            sum += functionalInterface.fun(nodes.get(i)) * weight.get(i) * det;
        }
        return sum;
    }




    public static NodesAndCoefficients nodesAndCoefficientsOfGaussianLagrangeQuadrature(int numberOfIntegrationPoints ){
        List<Double> nodes = new ArrayList<>();
        List<Double> coefficients = new ArrayList<>();

        if (numberOfIntegrationPoints == 2) {
            nodes.add(1 / Math.sqrt(3));
            coefficients.add(1.0);
            nodes.add(-1 / Math.sqrt(3));
            coefficients.add(1.0);
        } else if (numberOfIntegrationPoints == 3) {
            nodes.add(Math.sqrt(3.0 / 5.0));
            coefficients.add(5.0 / 9.0);
            nodes.add(0.0);
            coefficients.add(8.0 / 9.0);
            nodes.add(-Math.sqrt(3.0 / 5.0));
            coefficients.add(5.0 / 9.0);
        } else if (numberOfIntegrationPoints == 4) {
            nodes.add(-0.861136);
            coefficients.add(0.347855);
            nodes.add(-0.339981);
            coefficients.add(0.652145);
            nodes.add(0.339981);
            coefficients.add(0.652145);
            nodes.add(0.861136);
            coefficients.add(0.347855);
        } else if (numberOfIntegrationPoints == 5) {
            nodes.add(-0.906180);
            coefficients.add(0.236927);
            nodes.add(-0.538469);
            coefficients.add(0.478629);
            nodes.add(0.0);
            coefficients.add(0.568889);
            nodes.add(0.538469);
            coefficients.add(0.478629);
            nodes.add(0.906180);
            coefficients.add(0.236927);
        } else {
            nodes = null;
            coefficients = null;
            System.out.println("Invalid n");
        }

        Point[] nodePoints = new Point[nodes.size() * nodes.size()];
        Point[] coefficientPoints = new Point[coefficients.size() * coefficients.size()];

        int counter = 0;
        for(int i = 0; i < nodes.size(); i++){
            for(int j = 0; j < nodes.size(); j++){
                nodePoints[counter] = new Point(nodes.get(i), nodes.get(j));
                coefficientPoints[counter] = new Point(coefficients.get(i), coefficients.get(j));
                counter++;
            }
        }
        NodesAndCoefficients nodesAndCoefficients = new NodesAndCoefficients(nodePoints, coefficientPoints);
        return nodesAndCoefficients;
    }

    public static double[] nodesOfGaussianLagrangeQuadrature(int numberOfIntegrationPoints ){
        List<Double> nodes = new ArrayList<>();
        double [] nodesArray = new double[numberOfIntegrationPoints];

        if (numberOfIntegrationPoints == 2) {
            nodes.add(1 / Math.sqrt(3));
            nodes.add(-1 / Math.sqrt(3));
            for(int i = 0; i < nodes.size(); i++){
                nodesArray[i] = nodes.get(i);
            }
        } else if (numberOfIntegrationPoints == 3) {
            nodes.add(Math.sqrt(3.0 / 5.0));
            nodes.add(0.0);
            nodes.add(-Math.sqrt(3.0 / 5.0));
            for(int i = 0; i < nodes.size(); i++){
                nodesArray[i] = nodes.get(i);
            }
        } else if (numberOfIntegrationPoints == 4) {
            nodes.add(-0.861136);
            nodes.add(-0.339981);
            nodes.add(0.339981);
            nodes.add(0.861136);
            for(int i = 0; i < nodes.size(); i++){
                nodesArray[i] = nodes.get(i);
            }
        } else if (numberOfIntegrationPoints == 5) {
            nodes.add(-0.906180);
            nodes.add(-0.538469);
            nodes.add(0.0);
            nodes.add(0.538469);
            nodes.add(0.906180);
            for(int i = 0; i < nodes.size(); i++){
                nodesArray[i] = nodes.get(i);
            }
        } else {
            nodes = null;
            System.out.println("Invalid n");
        }

        return nodesArray;
    }

    public static double[][] coefficientsOfGaussianLagrangeQuadrature2(int numberOfIntegrationPoints ) {
        List<Double> coefficients = new ArrayList<>();
        double [][] coefficientsArray = null;
        if (numberOfIntegrationPoints == 2) {
            coefficients.add(1.0);
            coefficients.add(1.0);
            coefficientsArray = coefficients.toArray(coefficientsArray);
        } else if (numberOfIntegrationPoints == 3) {
            coefficients.add(5.0 / 9.0);
            coefficients.add(8.0 / 9.0);
            coefficients.add(5.0 / 9.0);
            coefficientsArray = coefficients.toArray(coefficientsArray);
        } else if (numberOfIntegrationPoints == 4) {
            coefficients.add(0.347855);
            coefficients.add(0.652145);
            coefficients.add(0.652145);
            coefficients.add(0.347855);
            coefficientsArray = coefficients.toArray(coefficientsArray);
        } else if (numberOfIntegrationPoints == 5) {
            coefficients.add(0.236927);
            coefficients.add(0.478629);
            coefficients.add(0.568889);
            coefficients.add(0.478629);
            coefficients.add(0.236927);
            coefficientsArray = coefficients.toArray(coefficientsArray);
        } else {
            coefficients = null;
            System.out.println("Invalid n");
        }
        return coefficientsArray;
    }


}

package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    @Data
    private static class Container{
        private List<Double> weights;
        private Point[] points;

        public Container(List<Double> weights, Point[] points) {
            this.weights = weights;
            this.points = points;
        }
    }
    public static Container pointsAndWeights(int numberOfIntegrationPoints) {
        //nPoints - zmienna wyznaczajaca ilos pounktow calkowania

        List<Double> nodes = new ArrayList<>();
        List<Double> weight = new ArrayList<>();
        
        if (numberOfIntegrationPoints == 2) {
            nodes.add(1 / Math.sqrt(3));
            weight.add(1.0);
            nodes.add(-1 / Math.sqrt(3));
            weight.add(1.0);
        } else if (numberOfIntegrationPoints == 3) {
            nodes.add(Math.sqrt(3.0 / 5.0));
            weight.add(5.0 / 9.0);
            nodes.add(0.0);
            weight.add(8.0 / 9.0);
            nodes.add(-Math.sqrt(3.0 / 5.0));
            weight.add(5.0 / 9.0);
        } else if (numberOfIntegrationPoints == 4) {
            nodes.add(-0.861136);
            weight.add(0.347855);
            nodes.add(-0.339981);
            weight.add(0.652145);
            nodes.add(0.339981);
            weight.add(0.652145);
            nodes.add(0.861136);
            weight.add(0.347855);
        } else if (numberOfIntegrationPoints == 5) {
            nodes.add(-0.906180);
            weight.add(0.236927);
            nodes.add(-0.538469);
            weight.add(0.478629);
            nodes.add(0.0);
            weight.add(0.568889);
            nodes.add(0.538469);
            weight.add(0.478629);
            nodes.add(0.906180);
            weight.add(0.236927);
        } else {
            nodes = null;
            weight = null;
            System.out.println("Invalid n");
        }

        Point[] points = new Point[nodes.size() * nodes.size()];

        int counter = 0;
        for(int i = 0; i < nodes.size(); i++){
            for(int j = 0; j < nodes.size(); j++){
                points[counter] = new Point(nodes.get(i), nodes.get(j));
                counter++;
            }
        }

        Container container = new Container(nodes, points);
        return container;
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

    public static void main(String[] args) {
        GlobalData globalData;
        Grid grid;
        String fileName = "test.txt";

        DataImporter dataImporter = new DataImporter();
        dataImporter.importData(fileName);
        globalData = dataImporter.getGlobalData();
        grid = dataImporter.getGrid();

        int numberOfIntegrationPoints = 2;//liczba punktow calkowania
        int nDSF= 4;//liczba funkcji ksztaltu(nazwa zmiennej do zmiany!!!)

        Container container = pointsAndWeights(numberOfIntegrationPoints);
        List<Double> weightFlag = container.getWeights();
        Point[] points = container.getPoints();

        /*
            Creation of universal element. Transform functions from Matrix class are being used to correct dN/dKsi and
            dN/dKsi 2d array.
        */
        UniversalElement universalElement = new UniversalElement(points, nDSF);
        universalElement.setNOverKsi(Matrix.transformHorizontally2dArray(universalElement.getNOverKsi()));
        universalElement.setNOverEta(Matrix.transformVertically2dArray(universalElement.getNOverEta()));
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////






        /////////////////////////////////////////
        //Obliczenia dla jednego punktu z pdf'a//
        /////////////////////////////////////////
        Node[] dummyNodes = new Node[4];
        dummyNodes[0] = new Node(0, 0);
        dummyNodes[1] = new Node(0.025, 0);
        dummyNodes[2] = new Node(0.025, 0.025);
        dummyNodes[3] = new Node(0, 0.025);

        double dummyConductivity = 30.0;

        for(int dummyCounter = 0; dummyCounter < dummyNodes.length; dummyCounter++) {




        /*
            TO EDIT!!!
            Wyznaczanie takiej malej macierzy sluzacej do przejscia z ukladu lokalnego do globalnego. Obliczenie
            wyznacznika macierzy i odwrotości wyznacznika macierzy(do poprawy te wyznaczniki bo nie sa liczone z klasy
            Matrix. Ogolnie ten kom tez do edycji.

                                   tutaj zmieniamy
                                           |
                                           |
                                           |
                                           V
            universalElement.getNOverKsi()[0][i];
            universalElement.getNOverEta()[0][i];


        */
            double x = 0.0;
            double y = 0.0;
            for (int i = 0; i < dummyNodes.length; i++) {
                x += dummyNodes[i].getX() * universalElement.getNOverKsi()[dummyCounter][i];
                y += dummyNodes[i].getY() * universalElement.getNOverEta()[dummyCounter][i];
            }
            double[][] miniMatrix = {{x, 0}, {0, y}};
            double detT = miniMatrix[0][0] * miniMatrix[1][1] - miniMatrix[0][1] * miniMatrix[1][0];
            double oneByDetT = 1.0 / detT;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
            Przejscie z ukladu lokalnego w globalny, z Ksi i Eta do x i y. Tutaj nic nie zmieniamy!!!
        */
            double[][] miniMatrixMultipliedByOneByDetT = Matrix.multiplyNumberBy2dArray(oneByDetT, miniMatrix);
            double nOverX[][] = new double[points.length][nDSF];
            double nOverY[][] = new double[points.length][nDSF];
            for (int i = 0; i < points.length; i++) {
                for (int j = 0; j < nDSF; j++) {
                    nOverX[i][j] = miniMatrixMultipliedByOneByDetT[0][1] * universalElement.getNOverKsi()[i][j] + miniMatrixMultipliedByOneByDetT[1][1] * universalElement.getNOverKsi()[i][j];
                    nOverY[i][j] = miniMatrixMultipliedByOneByDetT[0][0] * universalElement.getNOverEta()[i][j] + miniMatrixMultipliedByOneByDetT[1][0] * universalElement.getNOverEta()[i][j];
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
            Obliczanie macierzy H dla punktu calkowania.

                                   tutaj zmieniamy
                                           |
                                           |
                                           |
                                           V
                             Matrix.getRow(1, nOverY);
                             Matrix.getRow(1, nOverX);
        */
            double[][] yRow = Matrix.getRow(dummyCounter, nOverY);
            double[][] yCol = Matrix.replace2dArrayDimensions(yRow);
            double[][] xRow = Matrix.getRow(dummyCounter, nOverX);
            double[][] xCol = Matrix.replace2dArrayDimensions(xRow);
            double[][] yColumnMultipliedByYRow = Matrix.multiply2dArrays(yCol, yRow);
            double[][] xColumnMultipliedByXRow = Matrix.multiply2dArrays(xCol, xRow);
            double[][] H = Matrix.multiplyNumberBy2dArray(detT * dummyConductivity, Matrix.add2dArrays(xColumnMultipliedByXRow, yColumnMultipliedByYRow));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            System.out.println("H" + (dummyCounter + 1) + " ");
            Matrix.print2dArray(H);


        }













    }
}
package org.example;

import lombok.Data;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    @Data
    private static class Container{
        private List<Double> nodes = new ArrayList<>();
        private List<Double> weight = new ArrayList<>();
    }
    public static Container calculateNodesAndWeights(int nPoints) {
        //nPoints - zmienna wyznaczajaca ilos pounktow calkowania
        Container container = new Container();
        if (nPoints == 2) {
            container.getNodes().add(1 / Math.sqrt(3));
            container.getWeight().add(1.0);
            container.getNodes().add(-1 / Math.sqrt(3));
            container.getWeight().add(1.0);
        } else if (nPoints == 3) {
            container.getNodes().add(Math.sqrt(3.0 / 5.0));
            container.getWeight().add(5.0 / 9.0);
            container.getNodes().add(0.0);
            container.getWeight().add(8.0 / 9.0);
            container.getNodes().add(-Math.sqrt(3.0 / 5.0));
            container.getWeight().add(5.0 / 9.0);
        } else if (nPoints == 4) {
            container.getNodes().add(-0.861136);
            container.getWeight().add(0.347855);
            container.getNodes().add(-0.339981);
            container.getWeight().add(0.652145);
            container.getNodes().add(0.339981);
            container.getWeight().add(0.652145);
            container.getNodes().add(0.861136);
            container.getWeight().add(0.347855);
        } else if (nPoints == 5) {
            container.getNodes().add(-0.906180);
            container.getWeight().add(0.236927);
            container.getNodes().add(-0.538469);
            container.getWeight().add(0.478629);
            container.getNodes().add(0.0);
            container.getWeight().add(0.568889);
            container.getNodes().add(0.538469);
            container.getWeight().add(0.478629);
            container.getNodes().add(0.906180);
            container.getWeight().add(0.236927);
        } else {
            container.setNodes(null);
            container.setWeight(null);
            System.out.println("Invalid n");
        }
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

        int nPoints = 2;
        int nDSF= 4;
        Container container = calculateNodesAndWeights(nPoints);
        List<Double> nodesFlag = container.getNodes();
        List<Double> weightFlag = container.getWeight();

        //odpowiednik elemenu
        Node[] nodes = new Node[nodesFlag.size() * nodesFlag.size()];
        int counter = 0;
        for(int i = 0; i < nodesFlag.size(); i++){
            for(int j = 0; j < nodesFlag.size(); j++){
                nodes[counter] = new Node(nodesFlag.get(i), nodesFlag.get(j));
                counter++;
            }
        }

        double nOverKsi[][] = new double[nodes.length][nDSF];
        double nOverEta[][] = new double[nodes.length][nDSF];

        for(int i = 0; i < nodes.length; i ++){
            for(int j = 0; j < nDSF; j ++){
                if(j == 0){
                    nOverKsi[i][j] = -0.25 * (1 - nodes[i].getX());
                    nOverEta[i][j] = -0.25 * (1 - nodes[i].getY());
                }
                else if(j == 1){
                    nOverKsi[i][j] = 0.25 * (1 - nodes[i].getX());
                    nOverEta[i][j] = -0.25 * (1 + nodes[i].getY());
                }
                else if(j == 2){
                    nOverKsi[i][j] = 0.25 * (1 + nodes[i].getX());
                    nOverEta[i][j] = 0.25 * (1 + nodes[i].getY());
                }
                else if(j == 3){
                    nOverKsi[i][j] = -0.25 * (1 + nodes[i].getX());
                    nOverEta[i][j] = +0.25 * (1 - nodes[i].getY());
                }
            }
        }

        nOverKsi = Matrix.transformHorizontally2dArray(nOverKsi);
        nOverEta = Matrix.transformVertically2dArray(nOverEta);

        /////////////////////////////////////////
        //Obliczenia dla jednego punktu z pdf'a//
        /////////////////////////////////////////
        Node[] dummyNodes = new Node[4];
        dummyNodes[0] = new Node(0, 0);
        dummyNodes[1] = new Node(0.025, 0);
        dummyNodes[2] = new Node(0.025, 0.025);
        dummyNodes[3] = new Node(0, 0.025);

        double xOverKsi = 0.0;
        double yOverEta = 0.0;
        for(int i = 0; i < dummyNodes.length; i++){
            xOverKsi += dummyNodes[i].getX() * nOverKsi[0][i];
            yOverEta += dummyNodes[i].getY() * nOverEta[0][i];
        }

        double[][] miniMatrix = {{xOverKsi, 0},{0 ,yOverEta}};

        double detT = miniMatrix[0][0] * miniMatrix[1][1] - miniMatrix[0][1] * miniMatrix[1][0];
        double oneByDetT = 1.0 / detT;
        //do zmiany bo jest na sztywno

        double[][] miniMatrixMultipliedByOneByDetT = Matrix.multiplyNumberBy2dArray(oneByDetT, miniMatrix);

        double nOverX[][] = new double[nodes.length][nDSF];
        double nOverY[][] = new double[nodes.length][nDSF];

        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nDSF; j++){
                nOverX[i][j] = miniMatrixMultipliedByOneByDetT[0][1] * nOverKsi[i][j] + miniMatrixMultipliedByOneByDetT[1][1] * nOverKsi[i][j] ;
                nOverY[i][j] = miniMatrixMultipliedByOneByDetT[0][0] * nOverEta[i][j] + miniMatrixMultipliedByOneByDetT[1][0] * nOverEta[i][j] ;
            }
        }

        double dummyConductivity = 30.0;

        double [][] yRow = Matrix.getRow(0, nOverY);
        double [][] yCol = Matrix.replace2dArrayDimensions(yRow);

        double [][] xRow = Matrix.getRow(0, nOverX);
        double [][] xCol = Matrix.replace2dArrayDimensions(xRow);
        double [][] yColumnMultipliedByYRow = Matrix.multiply2dArrays(yCol, yRow);
        double [][] xColumnMultipliedByXRow = Matrix.multiply2dArrays(xCol, xRow);

        //Matrix.print2dArray(xColumnMultipliedByXRow);
        //Matrix.print2dArray(yColumnMultipliedByYRow);


        double [][] H = Matrix.multiplyNumberBy2dArray(detT * dummyConductivity, Matrix.add2dArrays(xColumnMultipliedByXRow, yColumnMultipliedByYRow));

        Matrix.print2dArray(H);
















    }
}
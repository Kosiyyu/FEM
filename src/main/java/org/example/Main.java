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
            System.exit(0);
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
        Matrix.print2dArray(nOverKsi);
        nOverEta = Matrix.transformVertically2dArray(nOverEta);
        Matrix.print2dArray(nOverEta);
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
        //to bedzie const (to co wyzej)
        //do dopisania bo to jest na sztywno :cc chociaz chyba i tak zadaziala

        double[][] miniMatrixMultipliedByOneByDetT = Matrix.multiplyNumberBy2dArray(oneByDetT, miniMatrix);

        double nOverX[][] = new double[nodes.length][nDSF];
        double nOverY[][] = new double[nodes.length][nDSF];

        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nDSF; j++){
                nOverX[i][j] = miniMatrixMultipliedByOneByDetT[0][1] * nOverKsi[i][j] + miniMatrixMultipliedByOneByDetT[1][1] * nOverKsi[i][j] ;
                //System.out.println(nOverX[i][j] + " ");
                nOverY[i][j] = miniMatrixMultipliedByOneByDetT[0][0] * nOverEta[i][j] + miniMatrixMultipliedByOneByDetT[1][0] * nOverEta[i][j] ;
            }
        }

        System.out.println("-------------------------------");
        System.out.println("nOverX:");
        Matrix.print2dArray(nOverX);
        System.out.println("-------------------------------");
        System.out.println("nOverY:");
        Matrix.print2dArray(nOverY);

        double dummyConductivity = 30.0;

        RealMatrix realMatrixX = new Array2DRowRealMatrix(nOverX);
        realMatrixX = new Array2DRowRealMatrix(realMatrixX.getRow(0));
        RealMatrix realMatrixY = new Array2DRowRealMatrix(nOverY);
        realMatrixY = new Array2DRowRealMatrix(realMatrixY.getRow(0));
        double [][] yCol = realMatrixY.getData();
        double [][] yRow = new double [yCol[0].length][yCol.length];

        int realRowY = 0;
        for(int i = 0; i < yRow[0].length;i++){
            yRow[realRowY][i] = yCol[i][realRowY];
        }

        double [][] xCol = realMatrixX.getData();
        double [][] xRow = new double [xCol[0].length][xCol.length];
        for(int i = 0; i < yCol.length; i++){
            xRow[yCol[0].length - 1][i] = xCol[i][xCol[0].length - 1];
        }

        RealMatrix yC = new Array2DRowRealMatrix(yCol);
        RealMatrix yR = new Array2DRowRealMatrix(yRow);
        RealMatrix xC = new Array2DRowRealMatrix(xCol);
        RealMatrix xR = new Array2DRowRealMatrix(xRow);

        RealMatrix xCxR = xC.multiply(xR);
        RealMatrix yCyR = yC.multiply(yR);

        RealMatrix sum = xCxR.add(yCyR);
        double [][] H = sum.getData();
        H = Matrix.multiplyNumberBy2dArray(detT * dummyConductivity, H);

        System.out.println("-------------------------------");
        System.out.println("H: ");
        Matrix.print2dArray(H);

        /*
        RealMatrix matrixB = new Array2DRowRealMatrix(bb2d);
        RealMatrix matrixC = new Array2DRowRealMatrix(cc2d);
        RealMatrix matrix = matrixB.multiply(matrixC);
        Matrix.print2dArray(matrix.getData());
        */












    }
}
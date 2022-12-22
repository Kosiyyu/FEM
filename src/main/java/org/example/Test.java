package org.example;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        double [][] matrix = {
                {9.0, 4.0, 3.0, 2.0},
                {8.0, 4.0, -1.0, 7.0},
                {4.0, 1.0, 1.0, 1.0},
                {3.0, 11.0, 2.0, -6.0}
        };

        double [][] vector = {
                {9.0},
                {9.0},
                {9.0},
                {9.0}
        };

        Gauss gauss = new Gauss(matrix, vector);
        //Matrix.print2dArray(gauss.calculate());

        UniversalElement universalElement = new UniversalElement(MathFunctions.nodesOfGaussianLagrangeQuadrature(2), MathFunctions.coefficientsOfGaussianLagrangeQuadrature2(2),4);
        double[][] flag = universalElement.getNShapeValue();
        //Matrix.print2dArray(flag);
        //Matrix.print2dArray(Matrix.getRow(0, flag));



        GlobalData globalData;
        Grid grid;
        String fileName = "test2.txt";

        DataImporter dataImporter = new DataImporter();
        dataImporter.importData(fileName);
        globalData = dataImporter.getGlobalData();
        grid = dataImporter.getGrid();

        EquationsSystem equationsSystem = new EquationsSystem(grid.getNumberOfNodes());

        int numberOfIntegrationPoints = 4;//liczba punktow calkowaniax
        int nDSF= 4;//liczba funkcji ksztaltu(nazwa zmiennej do zmiany!!!)

        for(int resultElementCounter = 0; resultElementCounter < grid.getElements().length; resultElementCounter++) {
            int []nodeIds = grid.getElements()[resultElementCounter].getNodeIds();
            Node[] resultNodes = new Node[4];
            for (int i = 0; i < resultNodes.length; i++){
                final int finalI = i;
                Node n = Arrays.stream(grid.getNodes()).collect(Collectors.toList()).stream()
                        .filter(node -> node.getNodeId() == nodeIds[finalI])
                        .findFirst()
                        .get();//error for null!!!
                resultNodes[i] = n;
            }

        double xOverKsi = 0.0;
        double yOverKsi = 0.0;
        double yOverEta = 0.0;
        double xOverEta = 0.0;
        for (int i = 0; i < resultNodes.length; i++) {
            xOverKsi += resultNodes[i].getX() * universalElement.getNOverKsi()[0][i];
            yOverKsi += resultNodes[i].getY() * universalElement.getNOverKsi()[0][i];
            yOverEta += resultNodes[i].getY() * universalElement.getNOverEta()[0][i];
            xOverEta += resultNodes[i].getX() * universalElement.getNOverEta()[0][i];
        }
        double[][] jacobi = {{xOverKsi, yOverKsi}, {xOverEta, yOverEta}};
        double determinant = jacobi[0][0] * jacobi[1][1] - jacobi[0][1] * jacobi[1][0];
        double[][] inverseJacobi = Matrix.multiplyNumberBy2dArray(1.0 / determinant, jacobi);
        double nOverX[][] = new double[numberOfIntegrationPoints * numberOfIntegrationPoints][nDSF];
        double nOverY[][] = new double[numberOfIntegrationPoints * numberOfIntegrationPoints][nDSF];
        for (int i = 0; i < numberOfIntegrationPoints * numberOfIntegrationPoints; i++) {
            for (int j = 0; j < nDSF; j++) {
                nOverX[i][j] = inverseJacobi[0][1] * universalElement.getNOverKsi()[i][j] + inverseJacobi[1][1] * universalElement.getNOverKsi()[i][j];
                nOverY[i][j] = inverseJacobi[0][0] * universalElement.getNOverEta()[i][j] + inverseJacobi[1][0] * universalElement.getNOverEta()[i][j];
            }
        }
            System.out.println(determinant);
    }
}}

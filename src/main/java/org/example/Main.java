package org.example;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    @Data
    private static class Container{
        private Point[] weights;
        private Point[] nodes;

        public Container(Point[] weights, Point[] nodes) {
            this.weights = weights;
            this.nodes = nodes;
        }
    }
    public static Container calculateNodesAndWeights(int numberOfIntegrationPoints) {
        //nPoints - zmienna wyznaczajaca ilos pounktow calkowania

        List<Double> nodes = new ArrayList<>();
        List<Double> weights = new ArrayList<>();

        if (numberOfIntegrationPoints == 2) {
            nodes.add(1 / Math.sqrt(3));
            weights.add(1.0);
            nodes.add(-1 / Math.sqrt(3));
            weights.add(1.0);
        } else if (numberOfIntegrationPoints == 3) {
            nodes.add(Math.sqrt(3.0 / 5.0));
            weights.add(5.0 / 9.0);
            nodes.add(0.0);
            weights.add(8.0 / 9.0);
            nodes.add(-Math.sqrt(3.0 / 5.0));
            weights.add(5.0 / 9.0);
        } else if (numberOfIntegrationPoints == 4) {
            nodes.add(-0.861136);
            weights.add(0.347855);
            nodes.add(-0.339981);
            weights.add(0.652145);
            nodes.add(0.339981);
            weights.add(0.652145);
            nodes.add(0.861136);
            weights.add(0.347855);
        } else if (numberOfIntegrationPoints == 5) {
            nodes.add(-0.906180);
            weights.add(0.236927);
            nodes.add(-0.538469);
            weights.add(0.478629);
            nodes.add(0.0);
            weights.add(0.568889);
            nodes.add(0.538469);
            weights.add(0.478629);
            nodes.add(0.906180);
            weights.add(0.236927);
        } else {
            nodes = null;
            weights = null;
            System.out.println("Invalid n");
        }

        Point[] nodePoints = new Point[nodes.size() * nodes.size()];
        Point[] weightPoints = new Point[weights.size() * weights.size()];

        int counter = 0;
        for(int i = 0; i < nodes.size(); i++){
            for(int j = 0; j < nodes.size(); j++){
                nodePoints[counter] = new Point(nodes.get(i), nodes.get(j));
                weightPoints[counter] = new Point(weights.get(i), weights.get(j));
                counter++;
            }
        }

        Container container = new Container(weightPoints, nodePoints);
        return container;
    }

    public static Container calculateNodesAndWeights2(int numberOfIntegrationPoints) {
        //nPoints - zmienna wyznaczajaca ilos pounktow calkowania

        List<Double> nodes = new ArrayList<>();
        List<Double> weights = new ArrayList<>();

        if (numberOfIntegrationPoints == 2) {
            nodes.add(1 / Math.sqrt(3));
            weights.add(1.0);
            nodes.add(-1 / Math.sqrt(3));
            weights.add(1.0);
        } else if (numberOfIntegrationPoints == 3) {
            nodes.add(Math.sqrt(3.0 / 5.0));
            weights.add(5.0 / 9.0);
            nodes.add(0.0);
            weights.add(8.0 / 9.0);
            nodes.add(-Math.sqrt(3.0 / 5.0));
            weights.add(5.0 / 9.0);
        } else if (numberOfIntegrationPoints == 4) {
            nodes.add(-0.861136);
            weights.add(0.347855);
            nodes.add(-0.339981);
            weights.add(0.652145);
            nodes.add(0.339981);
            weights.add(0.652145);
            nodes.add(0.861136);
            weights.add(0.347855);
        } else if (numberOfIntegrationPoints == 5) {
            nodes.add(-0.906180);
            weights.add(0.236927);
            nodes.add(-0.538469);
            weights.add(0.478629);
            nodes.add(0.0);
            weights.add(0.568889);
            nodes.add(0.538469);
            weights.add(0.478629);
            nodes.add(0.906180);
            weights.add(0.236927);
        } else {
            nodes = null;
            weights = null;
            System.out.println("Invalid n");
        }

        Point[] nodePoints = new Point[nodes.size() * nodes.size()];
        Point[] weightPoints = new Point[weights.size() * weights.size()];

        int counter = 0;
        for(int i = 0; i < nodes.size(); i++){
            for(int j = 0; j < nodes.size(); j++){
                nodePoints[counter] = new Point(nodes.get(i), nodes.get(j));
                weightPoints[counter] = new Point(weights.get(i), weights.get(j));
                counter++;
            }
        }

        Container container = new Container(weightPoints, nodePoints);
        return container;
    }

    public static double[][] calculateH(int index, double[][] nOverX, double[][] nOverY, Point[] weights,double determinant, double conductivity){
        double[][] yRow = Matrix.getRow(index, nOverY);
        double[][] yCol = Matrix.replace2dArrayDimensions(yRow);
        double[][] xRow = Matrix.getRow(index, nOverX);
        double[][] xCol = Matrix.replace2dArrayDimensions(xRow);
        double[][] yColumnMultipliedByYRow = Matrix.multiply2dArrays(yCol, yRow);
        double[][] xColumnMultipliedByXRow = Matrix.multiply2dArrays(xCol, xRow);
        double[][] H = Matrix.add2dArrays(xColumnMultipliedByXRow, yColumnMultipliedByYRow);
        H = Matrix.multiplyNumberBy2dArray(determinant * conductivity, H);
        H = Matrix.multiplyNumberBy2dArray(weights[index].x * weights[index].y, H);
        return H;
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

        int numberOfIntegrationPoints = 2;//liczba punktow calkowania
        int nDSF= 4;//liczba funkcji ksztaltu(nazwa zmiennej do zmiany!!!)

        DataImporter dataImporter = new DataImporter();
        dataImporter.importData(fileName);
        globalData = dataImporter.getGlobalData();
        grid = dataImporter.getGrid();

        EquationsSystem equationsSystem = new EquationsSystem(grid.getNumberOfNodes());

        Container container = calculateNodesAndWeights(numberOfIntegrationPoints);
        Point[] weights = container.getWeights();
        Point[] nodes = container.getNodes();

        UniversalElement universalElement = new UniversalElement(nodes, nDSF);
        universalElement.setNOverKsi(Matrix.transformHorizontally2dArray(universalElement.getNOverKsi()));
        universalElement.setNOverEta(Matrix.transformVertically2dArray(universalElement.getNOverEta()));

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

            double [][]resultH = new double[nDSF][nDSF];

            for(int resultNodesCounter = 0; resultNodesCounter < nodes.length; resultNodesCounter++) {

                double x = 0.0;
                double y = 0.0;
                for (int i = 0; i < resultNodes.length; i++) {
                    x += resultNodes[i].getX() * universalElement.getNOverKsi()[resultNodesCounter][i];
                    y += resultNodes[i].getY() * universalElement.getNOverEta()[resultNodesCounter][i];
                }
                double[][] jacobi = {{x, 0}, {0, y}};
                double determinant = jacobi[0][0] * jacobi[1][1] - jacobi[0][1] * jacobi[1][0];
                double[][] inverseJacobi = Matrix.multiplyNumberBy2dArray(1.0 / determinant, jacobi);
                double nOverX[][] = new double[nodes.length][nDSF];
                double nOverY[][] = new double[nodes.length][nDSF];
                for (int i = 0; i < nodes.length; i++) {
                    for (int j = 0; j < nDSF; j++) {
                        nOverX[i][j] = inverseJacobi[0][1] * universalElement.getNOverKsi()[i][j] + inverseJacobi[1][1] * universalElement.getNOverKsi()[i][j];
                        nOverY[i][j] = inverseJacobi[0][0] * universalElement.getNOverEta()[i][j] + inverseJacobi[1][0] * universalElement.getNOverEta()[i][j];
                    }
                }
                double[][] H = calculateH(resultNodesCounter, nOverX, nOverY, weights, determinant, globalData.getConductivity());
                resultH = Matrix.add2dArrays(resultH, H);
            }
            grid.getElements()[resultElementCounter].setH(resultH);//zapisywanie do elementow
            equationsSystem.add(grid.getElements()[resultElementCounter]);//wczytywanie do ukladu rownan
        }
        //Matrix.print2dArray(equationsSystem.getHG());

/*        container = calculateNodesAndWeights(numberOfIntegrationPoints);
        weights = container.getWeights();
        nodes = container.getNodes();
        universalElement = new UniversalElement(nodes, nDSF);*/

        weights = container.getWeights();
        nodes = container.getNodes();

        //DALA JEDNEJ SCIANY KTORA JEST SCIANA BRZEGOWA
        Point[] dummyPoints = {new Point(-1,0.5773), new Point(-1,-0.5773)};//0.7886
        double[] dummyWeights = {1 ,1};//0.7886
        Point[] dummyNodes = {new Point(0,0), new Point(0.025,0), new Point(0.025,0.025), new Point(0,0.025)};
        double[][] nArray = new double[dummyPoints.length][nDSF];

        double detJ = MathFunctions.distance(dummyNodes[0], dummyNodes[1]) / 2.0;
        double alfa = 25.0;
        int numberOfIntegrationPointsOnSide = dummyPoints.length;// to trzeba zmienic tak zeby ustalac z mienna i na tej podstawie ustawai sie od[pwiednia ilosc punkow calkowania w tabeli

        double [][] HBC = new double[nDSF][nDSF];

        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            double ksi = dummyPoints[i].x;
            double eta = dummyPoints[i].y;
            for (int j = 0; j < nDSF; j++) {
                if (j == 0) {
                    nArray[i][j] = 0.25 * (1 - ksi) * (1 - eta);
                } else if (j == 1) {
                    nArray[i][j] = 0.25 * (1 + ksi) * (1 - eta);
                } else if (j == 2) {
                    nArray[i][j] = 0.25 * (1 + ksi) * (1 + eta);
                } else if (j == 3) {
                    nArray[i][j] = 0.25 * (1 - ksi) * (1 + eta);
                }
            }

            double [][] row = Matrix.getRow(i,nArray);
            double [][] transRow = Matrix.replace2dArrayDimensions(Matrix.getRow(i,nArray));

            HBC = Matrix.add2dArrays(HBC, Matrix.multiplyNumberBy2dArray(detJ, Matrix.multiplyNumberBy2dArray(alfa * dummyWeights[i], Matrix.multiply2dArrays(transRow, row))));
        }
        Matrix.print2dArray(HBC);//dobrze wychodzi
        //tera trzeba powtorzyc dla innych bokow


        /*  BUG!!! DEL WHEN FIXED
        double [][] row = Matrix.getRow(0,nArray);
        Matrix.print2dArray(row);
        double [][] transRow = Matrix.replace2dArrayDimensions(Matrix.getRow(0,nArray));
        Matrix.print2dArray(transRow);
        Matrix.print2dArray(Matrix.replace2dArrayDimensions(transRow));
        //tu jest blaad dzial tylko z row -> col, a z col -> row to tak srednio
        //+
        //bug w funkcji do transponowania (replace2dArrayDimensions) w Matrix
        */





    }
}
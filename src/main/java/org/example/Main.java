package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static double[][] calculateH(int index, double[][] nOverX, double[][] nOverY, double[] weightsX, double[] weightsY, double determinant, double conductivity){
        double[][] yRow = Matrix.getRow(index, nOverY);
        double[][] yCol = Matrix.replace2dArrayDimensions(yRow);
        double[][] xRow = Matrix.getRow(index, nOverX);
        double[][] xCol = Matrix.replace2dArrayDimensions(xRow);
        double[][] yColumnMultipliedByYRow = Matrix.multiply2dArrays(yCol, yRow);
        double[][] xColumnMultipliedByXRow = Matrix.multiply2dArrays(xCol, xRow);
        double[][] H = Matrix.add2dArrays(xColumnMultipliedByXRow, yColumnMultipliedByYRow);
        H = Matrix.multiplyNumberBy2dArray(determinant * conductivity, H);
        H = Matrix.multiplyNumberBy2dArray(weightsX[index] * weightsY[index], H);
        return H;
    }

    public static double[][] calculateC(int index, double[][] nShapeValue, double[] weightsX, double[] weightsY, double determinant, double specificHeat, double density){
        double[][] row = Matrix.getRow(index, nShapeValue);
        double[][] col = Matrix.replace2dArrayDimensions(row);
        double[][] C = Matrix.multiply2dArrays(col, row);
        //System.out.println(determinant);
        //Matrix.print2dArray(nShapeValue);
        Matrix.print2dArray(row);
        C = Matrix.multiplyNumberBy2dArray(specificHeat * density * determinant, C);
        C = Matrix.multiplyNumberBy2dArray(weightsX[index] * weightsY[index], C);
        return C;
    }

    public static double[][] calculateHbc(int index, double[][] N, double weight, double determinant, double alfa){
        double[][] row = Matrix.getRow(index, N);
        double[][] col = Matrix.replace2dArrayDimensions(Matrix.getRow(index, N));
        double[][] Hbc = Matrix.multiplyNumberBy2dArray(determinant * alfa * weight, Matrix.multiply2dArrays(col, row));
        return Hbc;
    }

    public static double[][] calculateP(int index, double[][] N, double weight, double determinant, double alfa, double tot){
        double[][] col = Matrix.replace2dArrayDimensions(Matrix.getRow(index, N));
        double[][] P = Matrix.multiplyNumberBy2dArray(alfa * tot * determinant * weight, col);
//        double[][] row = Matrix.getRow(index, N);
//        double[][] col = Matrix.replace2dArrayDimensions(Matrix.getRow(index, N));
//        double[][] P = Matrix.multiplyNumberBy2dArray(determinant * alfa * weight * tot, Matrix.multiply2dArrays(col, row));
        return P;
    }



    public static void main(String[] args) {
        GlobalData globalData;
        Grid grid;
        String fileName = "test2.txt";

        DataImporter dataImporter = new DataImporter();
        dataImporter.importData(fileName);
        globalData = dataImporter.getGlobalData();
        grid = dataImporter.getGrid();

        EquationsSystem equationsSystem = new EquationsSystem(grid.getNumberOfNodes());

        int numberOfIntegrationPoints = 4;
        int numberOfIntegrationPointsOnSide = 4;
        int nDSF= 4;

        UniversalElement universalElement = new UniversalElement(Functions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPoints), Functions.weightsOfGaussianLagrangeQuadrature(numberOfIntegrationPoints), nDSF);

        for(int resultElementCounter = 0; resultElementCounter < grid.getElements().length; resultElementCounter++) {// petetla po kazdym punkcie calkowania
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
            double [][]resultC = new double[nDSF][nDSF];

            double deltaNOverDeltaX[][] = new double[numberOfIntegrationPoints * numberOfIntegrationPoints][nDSF];
            double deltaNOverDeltaY[][] = new double[numberOfIntegrationPoints * numberOfIntegrationPoints][nDSF];
            for(int resultNodesCounter = 0; resultNodesCounter < numberOfIntegrationPoints * numberOfIntegrationPoints; resultNodesCounter++) {//petla kazdym punkcie calkowania
                double xOverKsi = 0.0;
                double yOverKsi = 0.0;
                double yOverEta = 0.0;
                double xOverEta = 0.0;
                for (int i = 0; i < resultNodes.length; i++) {
                    xOverKsi += resultNodes[i].getX() * universalElement.getDeltaNOverDeltaKsi()[resultNodesCounter][i];//pcx
                    yOverKsi += resultNodes[i].getY() * universalElement.getDeltaNOverDeltaKsi()[resultNodesCounter][i];//pcy
                    yOverEta += resultNodes[i].getY() * universalElement.getDeltaNOverDeltaEta()[resultNodesCounter][i];
                    xOverEta += resultNodes[i].getX() * universalElement.getDeltaNOverDeltaEta()[resultNodesCounter][i];
                }
                double[][] jacobi = {{xOverKsi, yOverKsi}, {xOverEta, yOverEta}};
                double determinant = jacobi[0][0] * jacobi[1][1] - jacobi[0][1] * jacobi[1][0];
                double[][] inverseJacobi = {{yOverEta,-yOverKsi}, {-xOverEta, xOverKsi}};
                inverseJacobi = Matrix.multiplyNumberBy2dArray(1.0 / determinant, inverseJacobi);
                    for (int j = 0; j < nDSF; j++) {
                        deltaNOverDeltaX[resultNodesCounter][j] = inverseJacobi[0][0] * universalElement.getDeltaNOverDeltaKsi()[resultNodesCounter][j] + inverseJacobi[0][1] * universalElement.getDeltaNOverDeltaEta()[resultNodesCounter][j];
                        deltaNOverDeltaY[resultNodesCounter][j] = inverseJacobi[1][0] * universalElement.getDeltaNOverDeltaKsi()[resultNodesCounter][j] + inverseJacobi[1][1] * universalElement.getDeltaNOverDeltaEta()[resultNodesCounter][j];
                    }
                double[][] H = calculateH(resultNodesCounter, deltaNOverDeltaX, deltaNOverDeltaY, universalElement.getWeightsX(), universalElement.getWeightsY(), determinant, globalData.getConductivity());
                resultH = Matrix.add2dArrays(resultH, H);

                double[][] C = calculateC(resultNodesCounter, universalElement.getN(), universalElement.getWeightsX(), universalElement.getWeightsY(), determinant, globalData.getSpecificHeat(), globalData.getDensity());
                resultC = Matrix.add2dArrays(resultC, C);
            }
            grid.getElements()[resultElementCounter].setH(resultH);//zapisywanie do elementow
            equationsSystem.addH(grid.getElements()[resultElementCounter]);//wczytywanie do ukladu rownan

            grid.getElements()[resultElementCounter].setC(resultC);//zapisywanie do elementow
            equationsSystem.addC(grid.getElements()[resultElementCounter]);//wczytywanie do ukladu rownan
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ponizej warunki brzegowe liczymy

        double [] nodes2 = Functions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide);


        Point[] dummyPoints = new Point[numberOfIntegrationPointsOnSide * 4];
        double[] pointsX = new double[numberOfIntegrationPointsOnSide * 4];
        double[] pointsY = new double[numberOfIntegrationPointsOnSide * 4];

        int counter = 0;
        //bok dol
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            dummyPoints[counter] = new Point(nodes2[i], -1);
            pointsX[counter] = nodes2[i];
            pointsY[counter] = -1;
            counter++;
        }
        //bok prawo
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            dummyPoints[counter] = new Point(1, nodes2[i]);
            pointsX[counter] = 1;
            pointsY[counter] = nodes2[i];
            counter++;
        }
        //bok gora
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            dummyPoints[counter] = new Point(nodes2[i], 1);
            pointsX[counter] = nodes2[i];
            pointsY[counter] = 1;
            counter++;
        }
        //bok lewo
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            dummyPoints[counter] = new Point(-1, nodes2[i]);
            pointsX[counter] = -1;
            pointsY[counter] = nodes2[i];
            counter++;
        }


        double[][] nArray = new double[numberOfIntegrationPointsOnSide][nDSF];
        boolean bCFlag = false;


        //petla po elementach
        for(int elementId = 0; elementId < grid.getElements().length; elementId++) {
            //wyciaganei node'ow z danego elementu
            int[] nodeIds = grid.getElements()[elementId].getNodeIds();
            Node[] resultNodes = new Node[4];
            for (int i = 0; i < resultNodes.length; i++) {
                final int finalI = i;
                Node n = Arrays.stream(grid.getNodes()).collect(Collectors.toList()).stream()
                        .filter(node -> node.getNodeId() == nodeIds[finalI])
                        .findFirst()
                        .get();//error for null!!!
                resultNodes[i] = n;
            }

            //resultNodes = dummyNodes;



            //HBC dla kazdego boku
            double[][] resultHBC = new double[nDSF][nDSF];
            double[][] resultP = new double[nDSF][1];
            for (int x = 0; x < 4; x++) {//lece 4 razy bo dla kazdego boku
                double determinant;
                if (x != 3) {
                    determinant = Functions.distance(resultNodes[x], resultNodes[x + 1]) / 2.0;
                    if (resultNodes[x].getBC() == 1 && resultNodes[x + 1].getBC() == 1) {
                        bCFlag = true;
                    }
                } else {
                    determinant = Functions.distance(resultNodes[3], resultNodes[0]) / 2.0;
                    if (resultNodes[3].getBC() == 1 && resultNodes[0].getBC() == 1) {
                        bCFlag = true;
                    }
                }
                //jezeli warunek brzegowy wystepuje to liczmy HBC
                if (bCFlag) {
                    for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {// i lece po jednym boku po punktach calkowania
                        double ksi = pointsX[numberOfIntegrationPointsOnSide * x + i];
                        double eta = pointsY[numberOfIntegrationPointsOnSide * x + i];
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
                        double[][] Hbc = calculateHbc(i,nArray, Functions.weightsOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide)[i], determinant, globalData.getAlfa());
                        resultHBC = Matrix.add2dArrays(resultHBC, Hbc);
                    }



                    //tutaj operacje dla wektora P
                    for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {// i lece po jednym boku i licze dla niego nArray
                        double ksi = pointsX[numberOfIntegrationPointsOnSide * x + i];
                        double eta = pointsY[numberOfIntegrationPointsOnSide * x + i];
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
                        double[][] P = calculateP(i, nArray, Functions.weightsOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide)[i], determinant, globalData.getAlfa(), globalData.getTot());
                        resultP = Matrix.add2dArrays(resultP, P);
                    }
                    bCFlag = false;
                }
            }
            grid.getElements()[elementId].setHBC(resultHBC);
            equationsSystem.addHbc(grid.getElements()[elementId]);

            grid.getElements()[elementId].setP(resultP);
            equationsSystem.addP(grid.getElements()[elementId]);

        }
        Matrix.print2dArray(equationsSystem.getHG());//H + Hbc
        Matrix.print2dArray(equationsSystem.getCG());
        Matrix.print2dArray(equationsSystem.getPG());

        Gauss gauss = new Gauss(equationsSystem.getHG(), equationsSystem.getPG());

        double[][] t0 = Matrix.createTemperatureVector(grid.getNumberOfNodes(), globalData.getInitialTemp());
        double[][] newH;
        double[][] newP;
        double[][] t1;




        int limit = (int)((globalData.getSimulationTime() / globalData.getSimulationStepTime()));

        for(int i = 1; i < limit + 1 ;i++){
            newH = Matrix.add2dArrays(equationsSystem.getHG(), Matrix.multiplyNumberBy2dArray(1.0 / globalData.getSimulationStepTime(), equationsSystem.getCG()));
            //System.out.println(t0.length + " " + t0[0].length);
            newP = Matrix.add2dArrays(equationsSystem.getPG(), Matrix.multiply2dArrays( Matrix.multiplyNumberBy2dArray(1.0 / globalData.getSimulationStepTime(), equationsSystem.getCG()), t0));
            t1 = Gauss.calculate(newH, newP);
            //Matrix.print2dArray(t1);
            System.out.println(i + ". time: " + i * globalData.getSimulationStepTime() + ", min: " + Matrix.minValueInVector(t1) + ", max: " + Matrix.maxValueInVector(t1));
            t0 = t1;
        }
    }
}
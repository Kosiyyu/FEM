package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

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
        C = Matrix.multiplyNumberBy2dArray(determinant * specificHeat * density, C);
        C = Matrix.multiplyNumberBy2dArray(weightsX[index] * weightsY[index], C);
        return C;
    }

    public static void main(String[] args) {
        GlobalData globalData;
        Grid grid;
        String fileName = "test.txt";

        DataImporter dataImporter = new DataImporter();
        dataImporter.importData(fileName);
        globalData = dataImporter.getGlobalData();
        grid = dataImporter.getGrid();

        EquationsSystem equationsSystem = new EquationsSystem(grid.getNumberOfNodes());

        int numberOfIntegrationPoints = 4;//liczba punktow calkowaniax
        int nDSF= 4;//liczba funkcji ksztaltu(nazwa zmiennej do zmiany!!!)

        UniversalElement universalElement = new UniversalElement(MathFunctions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPoints), MathFunctions.coefficientsOfGaussianLagrangeQuadrature2(numberOfIntegrationPoints), nDSF);
        //universalElement.setNOverKsi(Matrix.transformHorizontally2dArray(universalElement.getNOverKsi()));
        //universalElement.setNOverEta(Matrix.transformVertically2dArray(universalElement.getNOverEta()));

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
            double [][]resultC = new double[nDSF][nDSF];

            for(int resultNodesCounter = 0; resultNodesCounter < numberOfIntegrationPoints * numberOfIntegrationPoints; resultNodesCounter++) {

                double xOverKsi = 0.0;
                double yOverKsi = 0.0;
                double yOverEta = 0.0;
                double xOverEta = 0.0;
                for (int i = 0; i < resultNodes.length; i++) {
                    xOverKsi += resultNodes[i].getX() * universalElement.getNOverKsi()[resultNodesCounter][i];
                    yOverKsi += resultNodes[i].getY() * universalElement.getNOverKsi()[resultNodesCounter][i];
                    yOverEta += resultNodes[i].getY() * universalElement.getNOverEta()[resultNodesCounter][i];
                    xOverEta += resultNodes[i].getX() * universalElement.getNOverEta()[resultNodesCounter][i];
                }
                //double[][] jacobi = {{xOverKsi, yOverKsi}, {xOverEta, yOverEta}};
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
                //double[][] H = calculateH(resultNodesCounter, nOverX, nOverY, weights, determinant, globalData.getConductivity());
                double[][] H = calculateH(resultNodesCounter, nOverX, nOverY, universalElement.getWeightsX(), universalElement.getWeightsY(), determinant, globalData.getConductivity());
                resultH = Matrix.add2dArrays(resultH, H);

                double[][] C = calculateC(resultNodesCounter, universalElement.getNShapeValue(), universalElement.getWeightsX(), universalElement.getWeightsY(), determinant, globalData.getSpecificHeat(), globalData.getDensity());
                resultC = Matrix.add2dArrays(resultC, C);

                Matrix.print2dArray(jacobi);
                System.out.println(determinant);
            }
            //Matrix.print2dArray(resultH);
            System.out.println("---");


            grid.getElements()[resultElementCounter].setH(resultH);//zapisywanie do elementow
            equationsSystem.addH(grid.getElements()[resultElementCounter]);//wczytywanie do ukladu rownan

            //Matrix.print2dArray(resultH);

            grid.getElements()[resultElementCounter].setC(resultC);//zapisywanie do elementow
            equationsSystem.addC(grid.getElements()[resultElementCounter]);//wczytywanie do ukladu rownan
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ponizej warunki brzegowe liczymy







        int numberOfIntegrationPointsOnSide = 2;// to trzeba zmienic tak zeby ustalac z mienna i na tej podstawie ustawai sie od[pwiednia ilosc punkow calkowania w tabeli

        double [] nodes2 = MathFunctions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide);


        Point[] dummyPoints = new Point[numberOfIntegrationPointsOnSide * 4];

        int counter = 0;
        //bok dol
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            dummyPoints[counter] = new Point(nodes2[i], -1);
            counter++;
        }
        //bok prawo
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            dummyPoints[counter] = new Point(1, nodes2[i]);
            counter++;
        }
        //bok gora
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            dummyPoints[counter] = new Point(nodes2[i], 1);
            counter++;
        }
        //bok lewo
        for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {//lece po jednym boku
            dummyPoints[counter] = new Point(-1, nodes2[i]);
            counter++;
        }


        double[][] nArray = new double[numberOfIntegrationPointsOnSide][nDSF];
        boolean bCFlag = false;


        //petla po elementach
        double[][] test = new double[4][1];
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
                double[][] HBC = new double[nDSF][nDSF];
                double[][] P = new double[nDSF][1];
                //System.out.println(x);

                double detJ;
                if (x != 3) {
                    detJ = MathFunctions.distance(resultNodes[x], resultNodes[x + 1]) / 2.0;
                    if (resultNodes[x].getBC() == 1 && resultNodes[x + 1].getBC() == 1) {
                        bCFlag = true;
                    }
                } else {
                    detJ = MathFunctions.distance(resultNodes[3], resultNodes[0]) / 2.0;
                    if (resultNodes[3].getBC() == 1 && resultNodes[0].getBC() == 1) {
                        bCFlag = true;
                    }
                }

                //jezeli warunek brzegowy wystepuje to liczmy HBC
                //System.out.println(bCFlag);


                if (bCFlag) {
                    double[][] row = new double[0][];
                    double[][] transRow = new double[0][];
                    for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {// i lece po jednym boku i licze dla niego nArray
                        double ksi = dummyPoints[numberOfIntegrationPointsOnSide * x + i].x;
                        double eta = dummyPoints[numberOfIntegrationPointsOnSide * x + i].y;
                        //System.out.println(ksi + " " + eta);
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
                        //Matrix.print2dArray(nArray);
                        row = Matrix.getRow(i, nArray);//nArray to macierz przmnozona przez ksi eta
                        transRow = Matrix.replace2dArrayDimensions(Matrix.getRow(i, nArray));
                        HBC = Matrix.add2dArrays(HBC, Matrix.multiplyNumberBy2dArray(detJ, Matrix.multiplyNumberBy2dArray(globalData.getAlfa() * MathFunctions.coefficientsOfGaussianLagrangeQuadrature2(numberOfIntegrationPointsOnSide)[i], Matrix.multiply2dArrays(transRow, row))));
                    }
                    //System.out.println(x);
                    //Matrix.print2dArray(nArray);
                    //Matrix.print2dArray(HBC);//dobrze wychodzi
                    resultHBC = Matrix.add2dArrays(resultHBC, HBC);



                    //tutaj operacje dla wektora P
                    for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {// i lece po jednym boku i licze dla niego nArray
                        double ksi = dummyPoints[numberOfIntegrationPointsOnSide * x + i].x;
                        double eta = dummyPoints[numberOfIntegrationPointsOnSide * x + i].y;
                        //System.out.println(ksi + " " + eta);
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
                        P = Matrix.replace2dArrayDimensions(Matrix.getRow(i, nArray));
                        P = Matrix.multiplyNumberBy2dArray(globalData.getAlfa() * globalData.getTot() * detJ * MathFunctions.coefficientsOfGaussianLagrangeQuadrature2(numberOfIntegrationPointsOnSide)[i], P);//na sztywno
                        //Matrix.print2dArray(P);
                        resultP = Matrix.add2dArrays(resultP, P);
                    }










                    bCFlag = false;
                }
            }
            //
            grid.getElements()[elementId].setHBC(resultHBC);
            equationsSystem.addHBC(grid.getElements()[elementId]);

            grid.getElements()[elementId].setP(resultP);
            equationsSystem.addP(grid.getElements()[elementId]);

        }
        //Matrix.print2dArray(equationsSystem.getHG());//pod dodaniu hbc
        //Matrix.print2dArray(equationsSystem.getCG());
        //Matrix.print2dArray(equationsSystem.getPG());

        Gauss gauss = new Gauss(equationsSystem.getHG(), equationsSystem.getPG());
        //Matrix.print2dArray(gauss.calculate());

/*        double[][] t0 = Matrix.createTemperatureVector(16, globalData.getInitialTemp());
        double[][] newH = Matrix.add2dArrays(equationsSystem.getHG(), Matrix.multiplyNumberBy2dArray(1.0 / globalData.getSimulationStepTime(), equationsSystem.getCG()));
        double[][] newP = Matrix.add2dArrays(equationsSystem.getPG(), Matrix.multiply2dArrays( Matrix.multiplyNumberBy2dArray(1.0 / globalData.getSimulationStepTime(), equationsSystem.getCG()), t0));
        double[][] t1 = Gauss.calculate(newH, newP);*/

        double[][] t0 = Matrix.createTemperatureVector(grid.getNumberOfNodes(), globalData.getInitialTemp());
        double[][] newH;
        double[][] newP;
        double[][] t1;




        int limit = 10;

        for(int i = 1; i < limit + 1 ;i++){
            newH = Matrix.add2dArrays(equationsSystem.getHG(), Matrix.multiplyNumberBy2dArray(1.0 / globalData.getSimulationStepTime(), equationsSystem.getCG()));
            //System.out.println(t0.length + " " + t0[0].length);
            newP = Matrix.add2dArrays(equationsSystem.getPG(), Matrix.multiply2dArrays( Matrix.multiplyNumberBy2dArray(1.0 / globalData.getSimulationStepTime(), equationsSystem.getCG()), t0));
            t1 = Gauss.calculate(newH, newP);
            //Matrix.print2dArray(t1);
            System.out.println(i + ". time: " + i * globalData.getSimulationStepTime() + ", min: " + Matrix.minValueInVector(t1) + ", max: " + Matrix.maxValueInVector(t1));
            t0 = t1;
        }









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
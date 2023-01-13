package org.example;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String fileName = "test3.txt";

        DataImporter dataImporter = new DataImporter();
        dataImporter.importData(fileName);
        GlobalData globalData = dataImporter.getGlobalData();
        Grid grid = dataImporter.getGrid();

        EquationSystem equationsSystem = new EquationSystem(grid.getNumberOfNodes());

        int numberOfIntegrationPoints = 2;//[2 - 5]
        int numberOfIntegrationPointsOnSide = 2;//[2 - 5]
        final int nDSF = 4;


        UniversalElement universalElement = new UniversalElement(Functions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPoints), Functions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide), Functions.weightsOfGaussianLagrangeQuadrature(numberOfIntegrationPoints), nDSF, numberOfIntegrationPointsOnSide);
        for (int resultElementCounter = 0; resultElementCounter < grid.getElements().length; resultElementCounter++) {// petetla po kazdym punkcie calkowania
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //FETCHING NODES BASED ON ID PER ONE ELEMENT
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            System.out.println("\033[0;93m" + "--Element: " + (resultElementCounter + 1) + "--" + "\033[0m");
            int[] nodeIds = grid.getElements()[resultElementCounter].getNodeIds();
            Node[] nodesFromSelectedElement = new Node[4];
            for (int i = 0; i < nodesFromSelectedElement.length; i++) {
                final int finalI = i;
                Node n = Arrays.stream(grid.getNodes()).collect(Collectors.toList()).stream()
                        .filter(node -> node.getNodeId() == nodeIds[finalI])
                        .findFirst()
                        .get();//error for null!!!
                nodesFromSelectedElement[i] = n;
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //CALCULATIONS FOR H AND C
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            double[][] resultH = new double[nDSF][nDSF];
            double[][] resultC = new double[nDSF][nDSF];
            double[][] dNBydX = new double[numberOfIntegrationPoints * numberOfIntegrationPoints][nDSF];
            double[][] dNBydY = new double[numberOfIntegrationPoints * numberOfIntegrationPoints][nDSF];
            for (int resultNodesCounter = 0; resultNodesCounter < numberOfIntegrationPoints * numberOfIntegrationPoints; resultNodesCounter++) {//petla kazdym punkcie calkowania
                double dxBydKsi = 0.0;
                double dyBydKsi = 0.0;
                double dyBydEta = 0.0;
                double dxBydEta = 0.0;
                for (int i = 0; i < nodesFromSelectedElement.length; i++) {
                    dxBydKsi += nodesFromSelectedElement[i].getX() * universalElement.getDNBydKsi()[resultNodesCounter][i];//pcx
                    dyBydKsi += nodesFromSelectedElement[i].getY() * universalElement.getDNBydKsi()[resultNodesCounter][i];//pcy
                    dyBydEta += nodesFromSelectedElement[i].getY() * universalElement.getDNBydEta()[resultNodesCounter][i];
                    dxBydEta += nodesFromSelectedElement[i].getX() * universalElement.getDNBydEta()[resultNodesCounter][i];
                }
                double[][] jacobi = {{dxBydKsi, dyBydKsi}, {dxBydEta, dyBydEta}};
                double determinant = jacobi[0][0] * jacobi[1][1] - jacobi[0][1] * jacobi[1][0];
                double[][] inverseJacobi = {{dyBydEta, -dyBydKsi}, {-dxBydEta, dxBydKsi}};
                inverseJacobi = Matrix.multiplyNumberBy2dArray(1.0 / determinant, inverseJacobi);
                for (int j = 0; j < nDSF; j++) {
                    dNBydX[resultNodesCounter][j] = inverseJacobi[0][0] * universalElement.getDNBydKsi()[resultNodesCounter][j] + inverseJacobi[0][1] * universalElement.getDNBydEta()[resultNodesCounter][j];
                    dNBydY[resultNodesCounter][j] = inverseJacobi[1][0] * universalElement.getDNBydKsi()[resultNodesCounter][j] + inverseJacobi[1][1] * universalElement.getDNBydEta()[resultNodesCounter][j];
                }
                double[][] H = Functions.calculateH(resultNodesCounter, dNBydX, dNBydY, universalElement.getWeightsX(), universalElement.getWeightsY(), determinant, globalData.getConductivity());
                //Matrix.print2dArray(H);
                resultH = Matrix.add2dArrays(resultH, H);

                double[][] C = Functions.calculateC(resultNodesCounter, universalElement.getN(), universalElement.getWeightsX(), universalElement.getWeightsY(), determinant, globalData.getSpecificHeat(), globalData.getDensity());
                //Matrix.print2dArray(C);
                resultC = Matrix.add2dArrays(resultC, C);
            }
            Matrix.print2dArray(resultH);
            //Matrix.print2dArray(resultC);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //CALCULATIONS FOR HBC AND P
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            double[][] resultHBC = new double[nDSF][nDSF];
            double[][] resultP = new double[nDSF][1];
            boolean bCFlag = false;
            for (int x = 0; x < 4; x++) {//lece 4 razy bo dla kazdego boku
                double determinant;
                //////////////////////////////////////////////
                //DETERMINING WHERE BOUNDARY CONDITION APPEARS
                //////////////////////////////////////////////
                if (x != 3) {
                    determinant = Functions.distance(nodesFromSelectedElement[x], nodesFromSelectedElement[x + 1]) / 2.0;
                    if (nodesFromSelectedElement[x].getBC() == 1 && nodesFromSelectedElement[x + 1].getBC() == 1) {
                        bCFlag = true;
                    }
                } else {
                    determinant = Functions.distance(nodesFromSelectedElement[3], nodesFromSelectedElement[0]) / 2.0;
                    if (nodesFromSelectedElement[3].getBC() == 1 && nodesFromSelectedElement[0].getBC() == 1) {
                        bCFlag = true;
                    }
                }
                //////////////////////////////////////////////
                //CALCULATIONS WHEN BOUNDARY CONDITION APPEARS
                //////////////////////////////////////////////
                if (bCFlag) {
                    for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {// i lece po jednym boku po punktach calkowania
                        double[][] nForHbcAndP = new double[numberOfIntegrationPointsOnSide][nDSF];
                        nForHbcAndP = UniversalElement.nForHbcAndP(i, nForHbcAndP, universalElement.getKsiForHbcAndP(), universalElement.getEtaForHbcAndP(), nDSF, x, numberOfIntegrationPointsOnSide);
                        double[][] Hbc = Functions.calculateHbc(i, nForHbcAndP, Functions.weightsOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide)[i], determinant, globalData.getAlfa());
                        //Matrix.print2dArray(Hbc);
                        resultHBC = Matrix.add2dArrays(resultHBC, Hbc);
                    }

                    for (int i = 0; i < numberOfIntegrationPointsOnSide; i++) {// i lece po jednym boku i licze dla niego nArray
                        double[][] nForHbcAndP = new double[numberOfIntegrationPointsOnSide][nDSF];
                        nForHbcAndP = UniversalElement.nForHbcAndP(i, nForHbcAndP, universalElement.getKsiForHbcAndP(), universalElement.getEtaForHbcAndP(), nDSF, x, numberOfIntegrationPointsOnSide);
                        double[][] P = Functions.calculateP(i, nForHbcAndP, Functions.weightsOfGaussianLagrangeQuadrature(numberOfIntegrationPointsOnSide)[i], determinant, globalData.getAlfa(), globalData.getTot());
                        //Matrix.print2dArray(P);
                        resultP = Matrix.add2dArrays(resultP, P);
                    }
                    bCFlag = false;
                }
                ////////////////////
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //WRITE TO ELEMENT AND AGGREGATION
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            grid.getElements()[resultElementCounter].setHBC(resultHBC);
            equationsSystem.addHbc(grid.getElements()[resultElementCounter]);

            grid.getElements()[resultElementCounter].setP(resultP);
            equationsSystem.addP(grid.getElements()[resultElementCounter]);

            grid.getElements()[resultElementCounter].setH(resultH);
            equationsSystem.addH(grid.getElements()[resultElementCounter]);

            grid.getElements()[resultElementCounter].setC(resultC);
            equationsSystem.addC(grid.getElements()[resultElementCounter]);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //SOLVING A SYSTEM OF EQUATIONS
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Matrix.print2dArray(equationsSystem.getHG());//print HG as aggregated H + Hbc

        //Matrix.print2dArray(equationsSystem.getCG());//print CG

        //Matrix.print2dArray(equationsSystem.getPG());//print PG
//System.out.println("\033[0;92m" + "--Element: " + (resultElementCounter + 1) + "--" + "\033[0m")
        System.out.println("\033[0;92m" + "RESULTS:" + "\033[0m");
        double[][] t0 = Matrix.createTemperatureVector(grid.getNumberOfNodes(), globalData.getInitialTemp());
        double[][] newH;
        double[][] newP;
        double[][] t1;
        int limit = (int) ((globalData.getSimulationTime() / globalData.getSimulationStepTime()));
        for (int i = 1; i < limit + 1; i++) {
            newH = Matrix.add2dArrays(equationsSystem.getHG(), Matrix.multiplyNumberBy2dArray(1.0 / globalData.getSimulationStepTime(), equationsSystem.getCG()));
            newP = Matrix.add2dArrays(equationsSystem.getPG(), Matrix.multiply2dArrays(Matrix.multiplyNumberBy2dArray(1.0 / globalData.getSimulationStepTime(), equationsSystem.getCG()), t0));
            t1 = Gauss.calculate(newH, newP);
            System.out.println("\033[0;92m" + i + ". time: " + i * globalData.getSimulationStepTime() + ", min: " + Matrix.minValueInVector(t1) + ", max: " + Matrix.maxValueInVector(t1) + "\033[0m");
            t0 = t1;
        }
    }
}
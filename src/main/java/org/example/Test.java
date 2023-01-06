package org.example;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Test {

    public static double N1(double ksi, double eta)
    {
        return 0.25 * (1 - ksi) * (1 - eta);
    }

    public static double N2(double ksi, double eta)
    {
        return 0.25 * (1 + ksi) * (1 - eta);
    }

    public static double N3(double ksi, double eta)
    {
        return 0.25 * (1 + ksi) * (1 + eta);
    }

    public static double N4(double ksi, double eta)
    {
        return 0.25 * (1 - ksi) * (1 + eta);
    }
    public static void main(String[] args) {
    //ZAD 1
        GlobalData globalData;
        Grid grid;
        String fileName = "test2.txt";

        DataImporter dataImporter = new DataImporter();
        dataImporter.importData(fileName);
        globalData = dataImporter.getGlobalData();
        grid = dataImporter.getGrid();

        EquationsSystem equationsSystem = new EquationsSystem(grid.getNumberOfNodes());

        int numberOfIntegrationPoints = 2;//liczba punktow calkowaniax
        int nDSF= 4;//liczba funkcji ksztaltu(nazwa zmiennej do zmiany!!!)

        UniversalElement universalElement = new UniversalElement(MathFunctions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPoints), MathFunctions.coefficientsOfGaussianLagrangeQuadrature2(numberOfIntegrationPoints), nDSF);

        Matrix.print2dArray((universalElement.getDeltaNOverDeltaKsi()));
        Matrix.print2dArray((universalElement.getDeltaNOverDeltaEta()));
        Matrix.print2dArray((universalElement.getN()));
        for(double d : universalElement.getPointsX()){
            System.out.println(d);
        }
        System.out.println("aaaaa");
        for(double d : universalElement.getPointsY()){
            System.out.println(d);
        }
        System.out.println("aaaaa");



//        double [][] a = {
//                {1,1,1,1},
//                {1,2,1,1},
//                {1,1,3,1},
//                {1,1,1,4}};
//
//        Matrix.print2dArray(a);
//        Matrix.print2dArray(Matrix.transformVertically2dArray(a));

    }
}

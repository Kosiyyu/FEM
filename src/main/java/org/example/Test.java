package org.example;

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

        UniversalElement universalElement = new UniversalElement(MathFunctions.nodesOfGaussianLagrangeQuadrature(numberOfIntegrationPoints), MathFunctions.weightsOfGaussianLagrangeQuadrature(numberOfIntegrationPoints), nDSF);

        Matrix.print2dArray((universalElement.getDeltaNOverDeltaKsi()));
        Matrix.print2dArray((universalElement.getDeltaNOverDeltaEta()));
        Matrix.print2dArray((universalElement.getN()));
        for(double d : universalElement.getEta()){
            System.out.println(d);
        }
        System.out.println("aaaaa");
        for(double d : universalElement.getKsi()){
            System.out.println(d);
        }
        System.out.println("aaaaa");
        System.out.println("alalalalal");
    }
}

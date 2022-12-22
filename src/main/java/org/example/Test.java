package org.example;

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
        Matrix.print2dArray(gauss.calculate());

        UniversalElement universalElement = new UniversalElement(MathFunctions.nodesOfGaussianLagrangeQuadrature(2), MathFunctions.coefficientsOfGaussianLagrangeQuadrature2(2),4);
        double[][] flag = universalElement.getNShapeValue();
        Matrix.print2dArray(flag);
        Matrix.print2dArray(Matrix.getRow(0, flag));

    }
}

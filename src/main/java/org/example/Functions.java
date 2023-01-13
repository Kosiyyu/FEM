package org.example;

public class Functions {

    public static double[][] calculateH(int index, double[][] dnBydX, double[][] dnBydY, double[] weightsX, double[] weightsY, double determinant, double conductivity) {
        double[][] yRow = Matrix.getRow(index, dnBydY);
        double[][] yCol = Matrix.replace2dArrayDimensions(yRow);
        double[][] xRow = Matrix.getRow(index, dnBydX);
        double[][] xCol = Matrix.replace2dArrayDimensions(xRow);
        double[][] yColumnMultipliedByYRow = Matrix.multiply2dArrays(yCol, yRow);
        double[][] xColumnMultipliedByXRow = Matrix.multiply2dArrays(xCol, xRow);
        double[][] H = Matrix.add2dArrays(xColumnMultipliedByXRow, yColumnMultipliedByYRow);
        H = Matrix.multiplyNumberBy2dArray(determinant * conductivity * weightsX[index] * weightsY[index], H);
        return H;
    }

    public static double[][] calculateC(int index, double[][] n, double[] weightsX, double[] weightsY, double determinant, double specificHeat, double density) {
        double[][] row = Matrix.getRow(index, n);
        double[][] col = Matrix.replace2dArrayDimensions(row);
        double[][] C = Matrix.multiply2dArrays(col, row);
        C = Matrix.multiplyNumberBy2dArray(specificHeat * density * determinant * weightsX[index] * weightsY[index], C);
        return C;
    }

    public static double[][] calculateHbc(int index, double[][] n, double weight, double determinant, double alfa) {
        double[][] row = Matrix.getRow(index, n);
        double[][] col = Matrix.replace2dArrayDimensions(Matrix.getRow(index, n));
        double[][] Hbc = Matrix.multiplyNumberBy2dArray(determinant * alfa * weight, Matrix.multiply2dArrays(col, row));
        return Hbc;
    }

    public static double[][] calculateP(int index, double[][] n, double weight, double determinant, double alfa, double tot) {
        double[][] col = Matrix.replace2dArrayDimensions(Matrix.getRow(index, n));
        double[][] P = Matrix.multiplyNumberBy2dArray(alfa * tot * determinant * weight, col);
        return P;
    }

    public static double distance(Node p1, Node p2) {
        return Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));
    }

    public static double[] nodesOfGaussianLagrangeQuadrature(int numberOfIntegrationPoints) {
        double[] nodesArray = new double[numberOfIntegrationPoints];

        if (numberOfIntegrationPoints == 2) {
            nodesArray[0] = -1.0 / Math.sqrt(3.0);
            nodesArray[1] = 1.0 / Math.sqrt(3.0);
        } else if (numberOfIntegrationPoints == 3.0) {
            nodesArray[0] = -Math.sqrt(3.0 / 5.0);
            nodesArray[1] = 0.0;
            nodesArray[2] = Math.sqrt(3.0 / 5.0);
        } else if (numberOfIntegrationPoints == 4) {
            nodesArray[0] = -Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0)));
            nodesArray[1] = -Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0)));
            nodesArray[2] = Math.sqrt((3.0 / 7.0) - ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0)));
            nodesArray[3] = Math.sqrt((3.0 / 7.0) + ((2.0 / 7.0) * Math.sqrt(6.0 / 5.0)));
        } else if (numberOfIntegrationPoints == 5) {
            nodesArray[0] = -(1.0 / 3.0) * Math.sqrt(5.0 + (2.0 * Math.sqrt(10.0 / 7.0)));
            nodesArray[1] = -(1.0 / 3.0) * Math.sqrt(5.0 - (2.0 * Math.sqrt(10.0 / 7.0)));
            nodesArray[2] = 0.0;
            nodesArray[3] = (1.0 / 3.0) * Math.sqrt(5.0 - (2.0 * Math.sqrt(10.0 / 7.0)));
            nodesArray[4] = (1.0 / 3.0) * Math.sqrt(5.0 + (2.0 * Math.sqrt(10.0 / 7.0)));
        } else {
            nodesArray = null;
            System.out.println("Invalid n");
        }
        return nodesArray;
    }

    public static double[] weightsOfGaussianLagrangeQuadrature(int numberOfIntegrationPoints) {
        double[] weightsArray = new double[numberOfIntegrationPoints];
        if (numberOfIntegrationPoints == 2) {
            weightsArray[0] = 1.0;
            weightsArray[1] = 1.0;
        } else if (numberOfIntegrationPoints == 3) {
            weightsArray[0] = 5.0 / 9.0;
            weightsArray[1] = 8.0 / 9.0;
            weightsArray[2] = 5.0 / 9.0;
        } else if (numberOfIntegrationPoints == 4) {
            weightsArray[0] = (18.0 - Math.sqrt(30.0)) / 36.0;
            weightsArray[1] = (18.0 + Math.sqrt(30.0)) / 36.0;
            weightsArray[2] = (18.0 + Math.sqrt(30.0)) / 36.0;
            weightsArray[3] = (18.0 - Math.sqrt(30.0)) / 36.0;
        } else if (numberOfIntegrationPoints == 5) {
            weightsArray[0] = (322.0 - (13.0 * Math.sqrt(70.0))) / 900.0;
            weightsArray[1] = (322.0 + (13.0 * Math.sqrt(70.0))) / 900.0;
            weightsArray[2] = 128.0 / 225.0;
            weightsArray[3] = (322.0 + (13.0 * Math.sqrt(70.0))) / 900.0;
            weightsArray[4] = (322.0 - (13.0 * Math.sqrt(70.0))) / 900.0;
        } else {
            weightsArray = null;
            System.out.println("Invalid n");
        }
        return weightsArray;
    }


}

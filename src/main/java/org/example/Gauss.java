package org.example;

import lombok.Data;

@Data
public class Gauss {

    private static double [][] calculationMatrix;

    public Gauss(double [][] matrix, double[][] vector){
        calculationMatrix = new double[matrix.length][matrix[0].length + 1];
        for(int i = 0; i < calculationMatrix.length; i++){//rows
            for(int j = 0; j < calculationMatrix[0].length; j++){//columns
                if(j == calculationMatrix[0].length -1){
                    calculationMatrix[i][j] = vector[i][0];
                }
                else {
                    calculationMatrix[i][j] = matrix[i][j];
                }
            }
        }
    }

    public static double [][] elimination(){
        double [][] resultMatrix = calculationMatrix;
        for(int i = 0; i < calculationMatrix[0].length - 1; i++) {//columns
            for(int j = i + 1; j < calculationMatrix.length; j++) {//rows
                double[][] row = Matrix.getRow(i, resultMatrix);
                double[][] currentRow = Matrix.getRow(j, resultMatrix);
                if(currentRow[0][i] != 0){
                    double value = currentRow[0][i] / row[0][i];
                    currentRow = Matrix.subtract2dArrays(currentRow, Matrix.multiplyNumberBy2dArray(value, row));
                    calculationMatrix = Matrix.setRow(j, calculationMatrix, currentRow);
                }
            }
        }

        return resultMatrix;
    }

    public static double [][] backSubstitution(double[][] matrix){
        double [][] resultVector = new double[calculationMatrix.length][1];

        for (int i = matrix.length - 1; i >= 0; i--)//rows
        {
            resultVector[i][0] = matrix[i][matrix.length];
            for (int j = i + 1; j < matrix.length; j++)//columns
            {
                resultVector[i][0] -= matrix[i][j] * resultVector[j][0];
            }
            resultVector[i][0] /= matrix[i][i];
        }
        return resultVector;
    }


    public double[][] calculate(){
        return backSubstitution(elimination());
    }

    public static double[][] calculate(double [][] matrix, double[][] vector){
        calculationMatrix = new double[matrix.length][matrix[0].length + 1];
        for(int i = 0; i < calculationMatrix.length; i++){//rows
            for(int j = 0; j < calculationMatrix[0].length; j++){//columns
                if(j == calculationMatrix[0].length -1){
                    calculationMatrix[i][j] = vector[i][0];
                }
                else {
                    calculationMatrix[i][j] = matrix[i][j];
                }
            }
        }
        return backSubstitution(elimination());
    }
}
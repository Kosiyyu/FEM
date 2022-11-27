package org.example;

public class Matrix {

    public static double [][] getRow(int index, double[][] pass){
        double [][] arr = new double[1][pass[0].length];
        for(int i = 0; i < arr[0].length; i++){
            arr[0][i] = pass[index][i];
        }
        return arr;
    }

    public static double [][] getColumn(int index, double[][] pass){
        double [][] arr = new double[pass.length][1];
        for(int i = 0; i < arr.length; i++){
            arr[i][0] = pass[i][index];
        }
        return arr;
    }

    public static void print2dArray(double[][] arr) {
        System.out.println("--2dArray-->    " + "y: " + arr.length + ", x: " + arr[0].length);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static double[][] multiplyNumberBy2dArray(double num, double[][] pass){
        double[][] arr = new double[pass.length][pass[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = pass[i][j] * num;
            }
        }
        return arr;
    }

    public static void populate2dArrayWithZero(double[][] pass) {
        double[][] arr = new double[pass.length][pass[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = 0;
            }
        }
    }

    public static double[][] replace2dArrayDimensions(double[][] pass){
        double[][] arr = new double[pass[0].length][pass.length];
        for(int i = 0; i < arr.length; i++){
            arr[i][arr[0].length - 1] = pass[arr[0].length - 1][i];
        }
        return arr;
    }

    public static double[][] transformHorizontally2dArray(double[][] pass){
        double[][] arr = null;
        if(pass[0].length > 1) {
            arr = new double[pass.length][pass[0].length];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length / 2; j++) {
                    arr[i][j] = pass[i][arr[i].length - 1 - j];
                    arr[i][arr[i].length - 1 - j] = pass[i][j];
                }
            }
            System.out.println("Horizontal transform.");
        }
        else if(pass.length == 1 && pass[0].length == 1){
            arr = new double[pass.length][pass[0].length];
            arr[0][0] = pass[0][0];
            System.out.println("X and Y dimension are too small to conduct transform.");
        }
        else if(pass[0].length == 1){
            arr = new double[pass.length][pass[0].length];
            for (int i = 0; i < arr.length / 2; i++) {
                //System.out.println(arr[i][0]);
                arr[i][0] = pass[arr.length - 1 - i][0];
                arr[arr.length - 1 - i][0] = pass[i][0];
            }
            System.out.println("X dimension is too small to conduct transform.");
        }
        return arr;
    }

    public static double[][] transformVertically2dArray(double[][] pass){
        double[][] arr = null;
        if(pass.length > 1){
            arr = new double[pass.length][pass[0].length];
            for (int i = 0; i < arr.length / 2; i++) {
                for(int j = 0; j < arr[0].length; j++) {
                    arr[i][j] = pass[arr.length - 1 - i][j];
                    arr[arr.length - 1 - i][j] = pass[i][j];
                }
            }
            System.out.println("Vertical transform.");
        }
        else if(pass.length == 1 && pass[0].length == 1){
            arr = new double[pass.length][pass[0].length];
            arr[0][0] = pass[0][0];
            System.out.println("X and Y dimension are too small to conduct transform.");

        }
        else if(pass.length == 1){
            arr = new double[pass.length][pass[0].length];
            for (int j = 0; j < arr[0].length / 2; j++) {
                arr[0][j] = pass[0][arr[0].length - 1 - j];
                arr[0][arr[0].length - 1 - j] = pass[0][j];
            }
            System.out.println("Y dimension is too small to conduct transform.");
        }
        return arr;
    }

    public static double[][] subtract2dArrays(double[][] a, double[][] b){
        double[][] ab = new double[a.length][a[0].length];
        if(a.length == b.length && a[0].length  == b[0].length){
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    ab[i][j] = a[i][j] - b[i][j];
                }
            }
        }
        else {
            System.out.println("The size of the arrays is not the same.");
        }
        return ab;
    }

    public static double[][] add2dArrays(double[][] a, double[][] b){
        double[][] ab = new double[a.length][a[0].length];
        if(a.length == b.length && a[0].length  == b[0].length){
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    ab[i][j] = a[i][j] + b[i][j];
                }
            }
        }
        else {
            System.out.println("The size of the arrays is not the same.");
        }
        return ab;
    }

    public static double[][] multiply2dArrays(double a[][], double b[][]){//a[m][n], b[n][p]

        if(a[0].length != b.length){
            System.out.println("Invalid sizes");
            return null;
        }
        double ab[][] = new double[a.length][b[0].length];

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < b[0].length; j++){
                for(int k = 0; k < a[0].length; k++){
                    ab[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return ab;
    }

}


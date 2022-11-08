package ai.model;

import Jama.Matrix;

import java.io.Serializable;


public class InputLayer extends Layer implements Serializable {

    public InputLayer(int pSize) {
        super(pSize);
    }

    public double[] predictSymbol(int[][] bitmap) {
        return this.nextLayer.predict(bitMapToArray(bitmap)).transpose().getArray()[0];
    }

    public double[] learnSymbol(int[][] bitmap, double[] expected) {
        double[] result = this.nextLayer.learn(bitMapToArray(bitmap), new Matrix(expected, expected.length)).getArray()[0];
        this.nextLayer.adjustWeights();
        return result;
    }

    private Matrix bitMapToArray(int[][] bitMap) {
        double[] array = new double[bitMap.length * bitMap[0].length];
        for (int x = 0; x < bitMap.length; x++) {
            for (int y = 0; y < bitMap[0].length; y++) {
                array[x*bitMap.length + y] = bitMap[x][y];
            }
        }
        return new Matrix(array, array.length);
    }

    @Override
    public String toString() {
        String res = this.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(this)) + " | ";
        if (prevLayer != null) {
            res += prevLayer.getClass().getName() + "(PREV)@" + Integer.toHexString(System.identityHashCode(prevLayer)) + " | ";
        } else {
            res += "null | ";
        }
        if (nextLayer != null) {
            res += nextLayer.getClass().getName() + "(NEXT)@" + Integer.toHexString(System.identityHashCode(nextLayer));
        } else {
            res += "null | ";
        }
        return res;
    }
}

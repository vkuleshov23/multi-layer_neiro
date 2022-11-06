package ai.model;

import Jama.Matrix;

import java.util.Collections;
import java.util.Iterator;

public class InputLayer extends Layer {

    public InputLayer(int pSize) {
        super(pSize);
    }

    public double[] predictSymbol(int[][] bitmap) {
        return this.nextLayer.predict(bitMapToArray(bitmap)).getArray()[0];
    }

    public double[] learnSymbol(int[][] bitmap, double[] expected) {
        return this.nextLayer.learn(bitMapToArray(bitmap), new Matrix(expected, expected.length)).getArray()[0];
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
}

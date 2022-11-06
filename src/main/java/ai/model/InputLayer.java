package ai.model;

import java.util.Collections;
import java.util.Iterator;

public class InputLayer extends Layer {

    public InputLayer(int pSize) {
        super(pSize);
    }

    public double[] predictSymbol(int[][] bitmap) {
        return this.nextLayer.predict(bitMapToArray(bitmap));
    }

    public double[] learnSymbol(int[][] bitmap, double[] expected) {
        return this.nextLayer.learn(bitMapToArray(bitmap), expected);
    }

    private double[] bitMapToArray(int[][] bitMap) {
        double[] array = new double[bitMap.length * bitMap[0].length];
        for (int x = 0; x < bitMap.length; x++) {
            for (int y = 0; y < bitMap[0].length; y++) {
                array[x*bitMap.length + y] = bitMap[x][y];
            }
        }
        return array;
    }
}

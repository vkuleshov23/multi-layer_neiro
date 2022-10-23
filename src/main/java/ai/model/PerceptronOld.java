package ai.model;

import ai.util.Consts;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
public class PerceptronOld implements Serializable {
    private final String perceptronSymbol;
    private final double[][] pixelWeight;

    public double getSum(int[][] bitMap, double referenceSum) {
        double sum = 0.0;
        for (int y = 0; y < pixelWeight[0].length; y++) {
            for (int x = 0; x < pixelWeight.length; x++) {
                sum += bitMap[x][y] * pixelWeight[x][y];
            }
        }
        return sum/referenceSum;
    }

    public boolean equalsBySymbol(String symbol) {
        return perceptronSymbol.equals(symbol);
    }

    public void punish(int[][] bitMap, double predict) {
        learn(bitMap, Consts.wrong_threshold - predict);
    }

    public void prise(int[][] bitMap, double predict) {
        learn(bitMap, Consts.recognize_threshold - predict);
    }

    public void punish(int[][] bitMap) {
        learn(bitMap, -Consts.step);
    }

    public void prise(int[][] bitMap) {
        learn(bitMap, Consts.step);
    }

    public void learn(int[][] bitmap, double step) {
        for (int y = 0; y < pixelWeight[0].length; y++) {
            for (int x = 0; x < pixelWeight.length; x++) {
                if (bitmap[x][y] == 1) {
                    pixelWeight[x][y] += Consts.rate *  step;
                    pixelWeight[x][y] = Math.min(pixelWeight[x][y], Consts.maxWeight);
                    pixelWeight[x][y] = Math.max(pixelWeight[x][y], Consts.minWeight);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerceptronOld that = (PerceptronOld) o;
        return Objects.equals(perceptronSymbol, that.perceptronSymbol);
    }

    @Override
    public int hashCode() {
        return perceptronSymbol.hashCode();
    }

    @Override
    public String toString() {

        StringBuilder res = new StringBuilder(perceptronSymbol + " {\n\t");
        for (int y = 0; y < pixelWeight[0].length; y++) {
            res.append(y).append("\t");
        }
        for (int x = 0; x < pixelWeight.length; x++) {
            res.append("\n").append(x).append("\t");
            for (int y = 0; y < pixelWeight[0].length; y++) {
                res.append("(").append(format(x,y)).append(")\t");
            }
        }
        res.append("}");
        return res.toString();
    }

    private String format(int x, int y) {
        if(pixelWeight[x][y] >= 0) {
            return String.format("+%.3f", pixelWeight[x][y]);
        }
        return String.format("%.3f", pixelWeight[x][y]);
    }

}

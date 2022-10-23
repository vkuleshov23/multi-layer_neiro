package ai.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class Core {

    private final int x;

    private final int y;

    private final double[] params;

    public Core(int x, int y) {
        this.x = x;
        this.y = y;
        this.params = new double[x*y];
    }

    public void setParamsArray(double[] params) {
        if(params.length != x*y) return;
        System.arraycopy(params, 0, this.params, 0, params.length);
    }

    public void setParams(double ... params) {
        if(params.length != x*y) return;
        System.arraycopy(params, 0, this.params, 0, params.length);
    }

    public void clear() {
        for (double param : params) {
            param = 0.0;
        }
    }

    public double getWeight() {
        return sum();
    }

    private double sum() {
        double sum = 0.0;
        for (double param : params) {
            sum += param;
        }
        return sum;
    }

}
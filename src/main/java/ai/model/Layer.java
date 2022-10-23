package ai.model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Layer {

    private static final Logger logger = LoggerFactory.getLogger(Layer.class);

    protected final List<Perceptron[]> perceptrons = new ArrayList<>();

    protected Layer prevLayer;

    protected Layer nextLayer;

    protected int pSize;

    public Layer(int pSize, Layer prevLayer) {
        this.prevLayer = prevLayer;
        prevLayer.setNextLayer(this);
        createPerceptrons(pSize);
    }

    public Layer(int pSize) {
        this.pSize = pSize;
    }

    protected void createPerceptrons(int pSize) {
        this.pSize = pSize;
        for (int i = 0; i < pSize; i++) {
            Perceptron[] ps = new Perceptron[prevLayer.pSize];
            for (int j = 0; j < prevLayer.pSize; j++) {
                ps[j] = new Perceptron();
            }
            perceptrons.add(ps);
        }
    }

    public double[] predict(double[] input) {
        double[] out = activation(sums(input));
        return nextLayer.predict(out);
    }

    public double[] learn(double[] input, double[] expected) {
        double[] out = activation(sums(input));
        return nextLayer.learn(out, expected);
    }

    protected double[] sums(double[] input) {
        double[] sums = new double[pSize];
        for (int i = 0; i < pSize; i++) {
            sums[i] = sum(perceptrons.get(i), input);
        }
        return sums;
    }

    protected double sum(Perceptron[] ps, double[] input) {
        double sum = 0.0;
        for (int i = 0; i < prevLayer.pSize; i++) {
            sum += ps[i].getWeight() * input[i];
        }
        return sum;
    }

    public double[] activation(double[] sums) {
        double[] res = new double[sums.length];
        for (int i = 0; i < sums.length; i++) {
            res[i] = activation(sums[i]);
        }
        return res;
    }

    public double activation(double sums) {
        return 1 / (1 + Math.exp(-sums));
    }

    public double[] derivativeActivation(double[] weights) {
        double[] res = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            res[i] = derivativeActivation(weights[i]);
        }
        return res;
    }

    public double derivativeActivation(double weight) {
        return activation(weight) - (1 - activation(weight));
    }

    protected double[] reversePropagation(double[] expected, double[] input, double[] out) {
        double[]
    }

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
        res += " -- " + perceptrons.size();
        return res;
    }

}

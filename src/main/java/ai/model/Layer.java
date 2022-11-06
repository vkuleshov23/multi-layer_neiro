package ai.model;

import Jama.Matrix;
import ai.util.Consts;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class Layer {

    private static final Logger logger = LoggerFactory.getLogger(Layer.class);

    protected final Matrix perceptrons;

    protected final Matrix deltas;

    protected Layer prevLayer;

    protected Layer nextLayer;

    protected int pSize;

    public Layer(int pSize, Layer prevLayer) {
        this.pSize = pSize;
        this.prevLayer = prevLayer;
        prevLayer.setNextLayer(this);
        perceptrons = new Matrix(this.pSize, prevLayer.pSize);
        deltas = new Matrix(prevLayer.pSize, this.pSize);
        randomizeMatrix();
    }

    protected void randomizeMatrix() {
        for (int i = 0; i < perceptrons.getRowDimension(); i++) {
            for (int j = 0; j < perceptrons.getColumnDimension(); j++) {
                perceptrons.set(i, j, (Math.random() * (Consts.maxNewWeight - Consts.minNewWeight)) + Consts.minNewWeight);
            }
        }
    }

    public Layer(int pSize) {
        perceptrons = null;
        deltas = null;
        this.pSize = pSize;
    }

    public Matrix predict(Matrix input) {
        Matrix out = activation(sum(input));
        return nextLayer.predict(out);
    }

    public Matrix learn(Matrix input, Matrix expected) {
        Matrix newInput = sum(input);
        Matrix out = activation(newInput);
        Matrix sigma = reversePropagation(expected, newInput, out);
//        adjustWeights(newInput, sigma);
        return sigma;
    }


    protected Matrix reversePropagation(Matrix expected, Matrix input, Matrix out) {
        Matrix sigmasOut = nextLayer.learn(out, expected);
        double[] sigmas = new double[pSize];
            for (int j = 0; j < pSize; j++) {
                for (int i = 0; i < sigmasOut.getRowDimension(); i++) {
//                    sigmas[j] += nextLayer.getPerceptrons().get(i)[j].getWeight() * sigmasOut[i];
                }
//            sigmas[j] *= derivativeActivation(input[j]);
            }
        return new Matrix(sigmas, 1);
    }

    protected void adjustWeights(double[] out, double[] sigma) {
        double[] dels = new double[prevLayer.pSize];
        for (int i = 0; i < sigma.length; i++) {
//            Perceptron[] ps = perceptrons.get(i);
//            double[] prevDels = getLastDeltas(i);
//            for (int j = 0; j < ps.length ; j++) {
//                dels[j] = ((1-Consts.rate) * Consts.speed * (sigma[i] * out[i])) + (Consts.rate * prevDels[j]);
//                ps[j].setWeight(ps[j].getWeight() + dels[j]);
//            }
//            setLastDeltas(i, dels);
        }
    }

//    protected double[] getLastDeltas(int i) {
//        try {
//            return deltas.get(i).getLast();
//        } catch (Exception e) {
//            return new double[prevLayer.pSize];
//        }
//    }

//    protected void setLastDeltas(int i, double[] dels) {
//        LinkedList<double[]> iDeltas = deltas.get(i);
//        if(iDeltas.size() > 10) {
//            iDeltas.removeFirst();
//        }
//        iDeltas.addLast(dels);
//    }

    protected Matrix sum(Matrix input) {
        return perceptrons.times(input);
    }


    public Matrix activation(Matrix a) {
        Matrix res = a.copy();
        for(int i = 0; i < res.getRowDimension(); i++) {
            for (int j = 0; j < res.getColumnDimension(); j++) {
                res.set(i, j, activation(res.get(i, j)));
            }
        }
        return res;
    }

    public double activation(double sum) {
        return 1 / (1 + Math.exp(-sum));
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
        res += " -- " + perceptrons.getColumnDimension();
        return res;
    }

}

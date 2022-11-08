package ai.model;

import Jama.Matrix;
import ai.util.Consts;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@Getter
@Setter
public class Layer implements Serializable {

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
        deltas = new Matrix(this.pSize, prevLayer.pSize);
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
        if(this.nextLayer != null) {
            return nextLayer.predict(out);
        } else {
            return out;
        }
    }

    public Matrix learn(Matrix input, Matrix expected) {
        Matrix newInput = sum(input);
        Matrix out = activation(newInput);
        Matrix sigma = reversePropagation(expected, newInput, out);
        updateDeltas(input, sigma);
        return sigma;
    }

    protected Matrix reversePropagation(Matrix expected, Matrix input, Matrix out) {
        Matrix sigmasOut = nextLayer.learn(out, expected);
        Matrix sigmas = new Matrix(pSize, 1);
        sigmas = this.nextLayer.perceptrons.transpose().times(sigmasOut);
        for (int j = 0; j < pSize; j++) {
            sigmas.set(j, 0, sigmas.get(j, 0) * derivativeActivation(input.get(j, 0)));
        }
        return sigmas;
    }

    protected void updateDeltas(Matrix input, Matrix sigma) {
        for (int i = 0; i < deltas.getRowDimension(); i++) {
            for (int j = 0; j < deltas.getColumnDimension(); j++) {
                deltas.set(i, j, (input.get(j, 0) * sigma.get(i, 0) * Consts.speed) );
            }
        }
    }

    protected void adjustWeights() {
        for (int i = 0; i < perceptrons.getRowDimension(); i++) {
            for (int j = 0; j < perceptrons.getColumnDimension(); j++) {
                perceptrons.set(i, j, perceptrons.get(i, j) - deltas.get(i, j) );
            }
        }
        if(this.nextLayer != null) {
            this.nextLayer.adjustWeights();
        }
    }

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

    public double derivativeActivation(double weight) {
        return activation(weight) * (1 - activation(weight));
    }

    public Matrix derivativeActivation(Matrix a) {
        Matrix res = a.copy();
        for(int i = 0; i < res.getRowDimension(); i++) {
            for (int j = 0; j < res.getColumnDimension(); j++) {
                res.set(i, j, derivativeActivation(res.get(i, j)));
            }
        }
        return res;
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
        res += " -- " + perceptrons.getColumnDimension();
        return res;
    }

}

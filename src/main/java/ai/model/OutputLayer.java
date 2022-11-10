package ai.model;

import Jama.Matrix;
import ai.service.ChartUpdater;
import ai.util.Consts;

import java.io.Serializable;

public class OutputLayer extends Layer implements Serializable {

    private int tp = 0, tn = 0, fp = 0, fn = 0;


    public OutputLayer(Layer prevLayer) {
        super(Consts.symbols.length, prevLayer);
    }


    @Override
    public void test(Matrix input, Matrix expected) {
        Matrix out = activation(sum(input));
        double sum = 0.0;
        Matrix sigma = out.minus(expected);
        for(int i = 0; i < sigma.getRowDimension(); i++) {
            for(int j = 0; j < sigma.getColumnDimension(); j++) {
                sigma.set(i, j, sigma.get(i, j) * derivativeActivation(input.get(i, j)));
                sum += sigma.get(i,j);
            }
        }
        calculateConfusions(out, expected);
        ChartUpdater.addLossData(Math.abs(sum));
        ChartUpdater.addAccData(getAccuracy());
        ChartUpdater.addPrecisionData(getPrecision());
        ChartUpdater.addRecallData(getRecall());
        ChartUpdater.addF1Data(getF1());
    }
    @Override
    protected Matrix reversePropagation(Matrix expected, Matrix input, Matrix out) {
        Matrix sigma = out.minus(expected);
        for(int i = 0; i < sigma.getRowDimension(); i++) {
            for(int j = 0; j < sigma.getColumnDimension(); j++) {
                sigma.set(i, j, sigma.get(i, j) * derivativeActivation(input.get(i, j)));
            }
        }
        return sigma;
    }

    private double getPrecision() {
        double res = (double) (tp) / (double) (tp + fp);
        return  Double.isNaN(res) ? 0.0 : res;
    }

    private double getRecall() {
        double res = (double) (tp) / (double) (tp + fn);
        return  Double.isNaN(res) ? 0.0 : res;
    }

    private double getF1() {
        double res = (2 * getPrecision() * getRecall()) / (getPrecision() + getRecall());
        return  Double.isNaN(res) ? 0.0 : res;
    }

    private double getAccuracy() {
        double res = (double) (tp + tn) / (double) (fn + fp + tn + tp);
        return  Double.isNaN(res) ? 0.0 : res;
    }

    private void calculateConfusions(Matrix out, Matrix expected) {
        tp = 0; tn = 0; fp = 0; fn = 0;
        for(int i = 0; i < out.getRowDimension(); i++) {
            for(int j = 0; j < out.getColumnDimension(); j++) {
                if(expected.get(i,j) == 0.0) {
                    if(out.get(i, j) > Consts.minThreshold) {
                        fp++;
                    } else {
                        tn++;
                    }
                } else {
                    if(out.get(i, j) < Consts.maxThreshold) {
                        fn++;
                    } else {
                        tp++;
                    }
                }
            }
        }
    }
}

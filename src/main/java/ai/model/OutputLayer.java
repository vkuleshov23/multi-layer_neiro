package ai.model;

import Jama.Matrix;
import ai.service.ChartUpdater;
import ai.util.Consts;

import java.io.Serializable;

public class OutputLayer extends Layer implements Serializable {


    public OutputLayer(Layer prevLayer) {
        super(Consts.symbols.length, prevLayer);
    }

    @Override
    protected Matrix reversePropagation(Matrix expected, Matrix input, Matrix out) {
        double sum = 0.0;
        Matrix sigma = out.minus(expected);
        for(int i = 0; i < sigma.getRowDimension(); i++) {
            for(int j = 0; j < sigma.getColumnDimension(); j++) {
//                sigma.set(i, j, Math.pow(sigma.get(i, j), 2));
                sigma.set(i, j, sigma.get(i, j) * derivativeActivation(input.get(i, j)));
                sum += sigma.get(i,j);
            }
        }
        ChartUpdater.addLossData(Math.abs(sum));
        ChartUpdater.addAccData(getAccuracy(out, expected));
        return sigma;
    }

    private double getAccuracy(Matrix out, Matrix expected) {
        int tp = 0, tn = 0, fp = 0, fn = 0;
        for(int i = 0; i < out.getRowDimension(); i++) {
            for(int j = 0; j < out.getColumnDimension(); j++) {
                if(expected.get(i,j) == 0.0) {
                    if(out.get(i, j) > 0.2) {
                        fp++;
                    } else {
                        tn++;
                    }
                } else {
                    if(out.get(i, j) < 0.8) {
                        fn++;
                    } else {
                        tp++;
                    }
                }
            }
        }
        return (double) (tp + tn) / (double) (fn + fp + tn + tp);
    }
}

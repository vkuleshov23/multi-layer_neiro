package ai.model;

import Jama.Matrix;
import ai.util.Consts;

public class OutputLayer extends Layer {

    public OutputLayer(Layer prevLayer) {
        super(Consts.symbols.length, prevLayer);
    }

    @Override
    public Matrix predict(Matrix input) {
        return activation(sum(input));
    }

//    @Override
//    public double[] learn(double[] input, double[] expected) {
//        double[] newInput = sums(input);
//        double[] out = activation(newInput);
//        double[] sigma = reversePropagation(expected, input, out);
//        adjustWeights(newInput, sigma);
//        return sigma;
//    }


    @Override
    protected Matrix reversePropagation(Matrix expected, Matrix input, Matrix out) {
        Matrix sigma = out.minus(expected);
        for(int i = 0; i < sigma.getRowDimension(); i++) {
            for(int j = 0; j < sigma.getRowDimension(); j++) {
                sigma.set(i, j, Math.pow(sigma.get(i, j), 2));
            }
        }
        return sigma;
    }

}

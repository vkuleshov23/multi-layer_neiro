package ai.model;

import ai.util.Consts;

public class OutputLayer extends Layer {

    public OutputLayer(Layer prevLayer) {
        super(1, prevLayer);
    }

    @Override
    public double[] predict(double[] input) {
        return activation(sums(input));
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
    protected double[] reversePropagation(double[] expected, double[] input, double[] out) {
        double[] sigma = new double[out.length];
        for(int i = 0; i < sigma.length; i++) {
            sigma[i] = Math.pow(out[i] - expected[i], 2) * derivativeActivation(input[i]);
        }
        return sigma;
    }

}

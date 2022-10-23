package ai.model;

public class OutputLayer extends Layer {

    public OutputLayer(Layer prevLayer) {
        super(1, prevLayer);
    }

    @Override
    public double[] predict(double[] input) {
        return activation(sums(input));
    }

    @Override
    public double[] learn(double[] input, double[] expected) {
        double[] out = activation(sums(input));
        return reversePropagation(expected, input, out);
    }

    @Override
    protected double[] reversePropagation(double[] expected, double[] input, double[] out) {
        double[] sigma = new double[out.length];
        for(int i = 0; i < sigma.length; i++) {
            sigma[i] = (expected[i] - out[i]) * derivativeActivation(input[i]);
        }
        return sigma;
    }

}

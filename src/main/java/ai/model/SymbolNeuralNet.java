package ai.model;

import ai.util.Consts;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

@Setter
@Getter
public class SymbolNeuralNet implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SymbolNeuralNet.class);

    private final LinkedList<Layer> layers = new LinkedList<>();

    public SymbolNeuralNet() {
        int size = Consts.xSize*Consts.ySize;
        Layer layer = new InputLayer(size);
        layers.add(layer); size /= 2;
        for (int i = 0; i < Consts.layersCount; i++) {
            layer = new Layer(size, layer);
            layers.add(layer); size /= 2;
        }
        layers.add(new OutputLayer(layer));
    }

    public double[] predict(int[][] bitmap) {
        return getInputLayer().predictSymbol(bitmap);
    }

    public double[] learn(int[][] bitmap, String writtenSymbol) {
        return getInputLayer().learnSymbol(bitmap, createExpected(writtenSymbol));
    }

    public void test(int[][] bitmap, String writtenSymbol) {
        getInputLayer().testSymbol(bitmap, createExpected(writtenSymbol));
    }

    private double[] createExpected(String writtenSymbol) {
        double[] expected = new double[Consts.symbols.length];
        for (int i = 0; i < expected.length; i++) {
            if (Objects.equals(Consts.symbols[i], writtenSymbol)) {
                expected[i] = 1.0;
            } else {
                expected[i] = 0.0;
            }
        }
        return expected;
    }

    public InputLayer getInputLayer() {
        return (InputLayer) layers.getFirst();
    }

    public OutputLayer getOutputLayer() {
        return (OutputLayer) layers.getLast();
    }

    public String toString() {
        StringBuilder res = new StringBuilder("---\n");
        for (Layer layer : layers) {
            res.append(layer.toString()).append("\n");
        }
        res.append("---\n");
        return res.toString();
    }

}

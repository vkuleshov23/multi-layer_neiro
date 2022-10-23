package ai.model;

import ai.util.Consts;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

@Setter
@Getter
public class SymbolNeuralNet {

    private static final Logger logger = LoggerFactory.getLogger(SymbolNeuralNet.class);

    private final LinkedList<Layer> layers = new LinkedList<>();

    private final String symbol;

    public SymbolNeuralNet(String symbol) {
        this.symbol = symbol;
        int size = Consts.xSize*Consts.ySize;
        Layer layer = new InputLayer(size);
        layers.add(layer); size /= 2;
        for (int i = 0; i < Consts.layersCount; i++) {
            layer = new Layer(size, layer);
            layers.add(layer); size /= 2;
        }
        layers.add(new OutputLayer(layer));
    }

    public double[] predict(int[][] bitmap, double referenceSum) {
        return getInputLayer().predictSymbol(bitmap);
    }

    public InputLayer getInputLayer() {
        return (InputLayer) layers.getFirst();
    }

    public OutputLayer getOutputLayer() {
        return (OutputLayer) layers.getLast();
    }

    public String toString() {
        StringBuilder res = new StringBuilder("--- " + symbol + " ---\n");
        for (Layer layer : layers) {
            res.append(layer.toString()).append("\n");
        }
        res.append("---\n");
        return res.toString();
    }

}

package ai.model;

import ai.util.Consts;
import ai.util.NewNeiroCreator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Layer {

    private static final Logger logger = LoggerFactory.getLogger(Layer.class);

    private final Map<String, List<Perceptron>> perceptronsMap;

    private final int width;

    private final int height;

    private final Core core = new Core(Consts.coreStep,Consts.coreStep);

    private double activation(double s) {
        return 1 / (1 + (Math.exp(-s)));
    }

    public Layer(int height, int width) {
        this.perceptronsMap = NewNeiroCreator.createPerceptrons(width*height);
        this.width = width;
        this.height = height;
    }

    public Layer(Layer prevLayer) {
        this(prevLayer.getHeight()/2, prevLayer.getWidth()/2);
    }

    public Map<String, List<Double>> getPredicts() {
        Map<String, List<Double>> predicts = new HashMap<>();
        for (String symbol: perceptronsMap.keySet()) {
            predicts.put(symbol, getPredicts(perceptronsMap.get(symbol)));
        }
        return predicts;
    }

    private List<Double> getPredicts(List<Perceptron> perceptrons) {
        List<Double> predicts = new ArrayList<>();
        int i = 0;
        for (int x = 0; x < height; x += Consts.coreStep) {
            for (int y = 0; y < width; y += Consts.coreStep) {
                fillCore(perceptrons, x, y);
                predicts.add(activation(core.getWeight()));
            }
        }
        return predicts;
    }

    private void fillCore(List<Perceptron> perceptrons, int x, int y) {
        double[] params = new double[core.getX() * core.getY()];
        for (int i = 0; i < Consts.coreStep; i++) {
            for (int j = 0; j < Consts.coreStep; j++) {
                params[i* Consts.coreStep + j] = perceptrons.get((x + i) * width + y + j).getWeight();
            }
        }
        core.clear();
        core.setParams(params);
    }

    public double loss(double[] predicts, double[] expected) {
        double[] softMaxPredicts = softMax(predicts);
        double sum = 0.0;
        for (int x = 0; x < softMaxPredicts.length; x++) {
            sum += Math.abs(expected[x] - softMaxPredicts[x]);
        }
        return sum;
    }

    public double[] softMax(double[] predicts) {
        double sumExp = getSumExp(predicts);
        double[] res = new double[predicts.length];
        for (int x = 0; x < predicts.length; x++) {
            res[x] += Math.exp(predicts[x]) / sumExp;
        }
        return res;
    }

    protected double getSumExp(double[] predicts) {
        double sum = 0.0;
        for (double predict : predicts) {
            sum += Math.exp(predict);
        }
        return sum;
    }

}

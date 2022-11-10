package ai.service;

import ai.model.SymbolNeuralNet;
import ai.util.Consts;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@Getter
@Setter
public class Neiro implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Neiro.class);

    private SymbolNeuralNet symbolNeuralNet = new SymbolNeuralNet();

    private static final Comparator<Pair<String, Double>> comparator = (double1, double2) -> double2.getValue().compareTo(double1.getValue());

    public List<Pair<String, Double>> predict(int[][] bitMap) {
       return toRes(this.symbolNeuralNet.predict(bitMap));
    }

    public List<Pair<String, Double>> learn(int[][] bitMap, String symbol) {
        return toRes(this.symbolNeuralNet.learn(bitMap, symbol));
    }

    public void test(int[][] bitMap, String symbol) {
        this.symbolNeuralNet.test(bitMap, symbol);
    }

    private List<Pair<String, Double>> toRes(double[] predict) {
        List<Pair<String, Double>> predicts = new LinkedList<>();
        for (int i = 0; i < predict.length; i++) {
            predicts.add(new Pair<>(Consts.symbols[i], predict[i]));
        }
        return predicts.stream().sorted(comparator).toList();
    }

    public void postConstruct() {
        this.symbolNeuralNet = new SymbolNeuralNet();
    }
}

package ai.service;

import ai.model.SymbolNeuralNet;
import ai.util.Consts;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@Getter
@Setter
public class Neiro {

    private static final Logger logger = LoggerFactory.getLogger(Neiro.class);

    private final SymbolNeuralNet symbolNeuralNet;

    private static final Comparator<Pair<String, Double>> comparator = (double1, double2) -> double2.getValue().compareTo(double1.getValue());

    public Neiro() {
        symbolNeuralNet = new SymbolNeuralNet();
    }

    public List<Pair<String, Double>> predict(int[][] bitMap, double referenceSum) {
       return toRes(this.symbolNeuralNet.predict(bitMap, referenceSum));
    }

    public List<Pair<String, Double>> learn(int[][] bitMap, double referenceSum, String symbol) {
        return toRes(this.symbolNeuralNet.learn(bitMap, referenceSum, symbol));
    }

    private List<Pair<String, Double>> toRes(double[] predict) {
        List<Pair<String, Double>> predicts = new LinkedList<>();
        for (int i = 0; i < predict.length; i++) {
            predicts.add(new Pair<>(Consts.symbols[i], predict[i]));
        }
        return predicts.stream().sorted(comparator).toList();
    }

    public void test() {
        System.out.println(symbolNeuralNet);
    }
}

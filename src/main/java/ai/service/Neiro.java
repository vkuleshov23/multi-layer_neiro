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
import java.util.List;

@Service
@Getter
@Setter
public class Neiro {

    private static final Logger logger = LoggerFactory.getLogger(Neiro.class);

    private final List<SymbolNeuralNet> symbolNeuralNets = new ArrayList<>();

    private static final Comparator<Pair<String, Double>> comparator = (double1, double2) -> double2.getValue().compareTo(double1.getValue());

    public Neiro() {
        for (String symbol : Consts.symbols) {
            symbolNeuralNets.add(new SymbolNeuralNet(symbol));
        }
    }

    public List<Pair<String, Double>> predict(int[][] bitMap, double referenceSum) {
        return this.symbolNeuralNets.stream().map(
                        net -> new Pair<>(net.getSymbol(), net.predict(bitMap, referenceSum)))
                .sorted(comparator).toList();
    }

    public void test() {
        for (SymbolNeuralNet net : this.symbolNeuralNets) {
            System.out.println(net);
        }
    }
}

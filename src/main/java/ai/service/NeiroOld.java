package ai.service;

import ai.model.PerceptronOld;
import ai.util.NewNeiroCreator;
import javafx.util.Pair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@Getter
@Setter
@RequiredArgsConstructor
public class NeiroOld implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(NeiroOld.class);

    private List<PerceptronOld> perceptronOlds = new ArrayList<>();

    public void createNewPerceptrones() {
        this.perceptronOlds = NewNeiroCreator.createPerceptronsOld();
    }

    public PerceptronOld findBySymbol(String symbol) throws Exception {
        return perceptronOlds.stream()
                .filter(perceptronOld -> perceptronOld.equalsBySymbol(symbol))
                .findAny().orElseThrow(() -> new Exception("No such perceptron exist!"));
    }

    public List<PerceptronOld> findOther(String symbol) {
        return perceptronOlds.stream()
                .filter(p -> !p.equalsBySymbol(symbol)).toList();
    }

    public void learn(PerceptronOld perceptronOld, int[][] bitMap, double referenceSum, String symbol) {
        double predict = perceptronOld.getSum(bitMap, referenceSum);
        if (perceptronOld.equalsBySymbol(symbol)) {
            perceptronOld.prise(bitMap, predict);
        } else {
            perceptronOld.punish(bitMap, predict);
        }

    }

    public void prise(int[][] bitMap, String symbol) throws Exception {
        findBySymbol(symbol).prise(bitMap);
    }

    public void punish(int[][] bitMap, String symbol) throws Exception {
        findBySymbol(symbol).punish(bitMap);
    }

    public List<Pair<String, Double>> getPredicts(int[][] bitMap, double referenceSum) {
        Comparator<Pair<String, Double>> comparator = (double1, double2) -> double2.getValue().compareTo(double1.getValue());
        return perceptronOlds.stream().map(
                        perceptronOld -> new Pair<>(perceptronOld.getPerceptronSymbol(), perceptronOld.getSum(bitMap, referenceSum)))
                .sorted(comparator).toList();
    }

    public void print() {
        perceptronOlds.forEach(perceptronOld ->  logger.info(perceptronOld.toString()));
    }

    public void postConstruct() {
        createNewPerceptrones();
    }

}

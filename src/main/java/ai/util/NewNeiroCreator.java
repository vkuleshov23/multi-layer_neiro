package ai.util;

import ai.model.Perceptron;
import ai.model.PerceptronOld;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class NewNeiroCreator {

    public List<PerceptronOld> createPerceptronsOld() {
        List<PerceptronOld> perceptronOlds = new ArrayList<>();
        for (String symbol : Consts.symbols) {
            perceptronOlds.add(new PerceptronOld(symbol, pixels()));
        }
        return perceptronOlds;
    }

    public Map<String, List<Perceptron>> createPerceptrons(int size) {
        Map<String, List<Perceptron>> map = new ConcurrentHashMap<>();
        for (String symbol : Consts.symbols) {
            List<Perceptron> perceptrons = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                perceptrons.add(new Perceptron(symbol, Consts.minWeight));
            }
            map.put(symbol, perceptrons);
        }
        return map;
    }

    public Map<String, List<Perceptron>> createPerceptronsRand(int size) {
        Map<String, List<Perceptron>> map = new ConcurrentHashMap<>();
        for (String symbol : Consts.symbols) {
            List<Perceptron> perceptrons = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                perceptrons.add(new Perceptron(symbol, Math.random()));
            }
            map.put(symbol, perceptrons);
        }
        return map;
    }

    private double[][] pixels() {
        double[][] pixelsWeight = new double[Consts.xSize][Consts.ySize];
        for (Integer x = 0; x < Consts.xSize; x++) {
            for (Integer y = 0; y < Consts.ySize; y++) {
//                pixelsWeight[x][y] = Randomizer.randInt();
                pixelsWeight[x][y] = Consts.minWeight;
            }
        }
        return pixelsWeight;
    }
}

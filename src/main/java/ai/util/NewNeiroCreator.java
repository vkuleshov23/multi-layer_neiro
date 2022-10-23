package ai.util;

import ai.model.PerceptronOld;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class NewNeiroCreator {

    public List<PerceptronOld> createPerceptronsOld() {
        List<PerceptronOld> perceptronOlds = new ArrayList<>();
        for (String symbol : Consts.symbols) {
            perceptronOlds.add(new PerceptronOld(symbol, pixels()));
        }
        return perceptronOlds;
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

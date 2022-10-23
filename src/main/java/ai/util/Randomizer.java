package ai.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Randomizer {
    public int randInt(int min, int max) {
        return min + (int)(Math.random() * (max - min));
    }

    public String randSymbol() {
        return Consts.symbols[Randomizer.randInt(0, Consts.symbols.length - 1)];
    }

    public String randString(String[] src) {
        return src[Randomizer.randInt(0, Consts.symbols.length - 1)];
    }

}

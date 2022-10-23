package ai.model;

import ai.util.Consts;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Perceptron {

    protected double weight;

    public Perceptron() {
        this.weight =  (Math.random() * (Consts.maxNewWeight - Consts.minNewWeight)) + Consts.minNewWeight;
    }

    public String toString() {
        return String.valueOf(weight);
    }
}

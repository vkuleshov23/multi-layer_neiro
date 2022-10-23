package ai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Perceptron {

    protected final String perceptronSymbol;

    protected final double weight;

}

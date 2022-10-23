package ai.service;

import ai.model.Layer;
import ai.model.Perceptron;
import ai.util.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class Neiro {

    private final Logger logger = LoggerFactory.getLogger(Neiro.class);
    private final LinkedList<Layer> layers = new LinkedList<>();

    public Neiro() {
        if (Consts.layersCount < 1) return;
        layers.addLast(new Layer(Consts.xSize,Consts.ySize));
        for (int i = 0; i < Consts.layersCount-1; i++) {
            layers.addLast(new Layer(layers.getLast()));
        }
    }


    public void test() {
        for (Layer layer : layers) {
            for (List<Perceptron> perceptrons : layer.getPerceptronsMap().values()) {
                logger.info(String.valueOf(perceptrons.size()));
//                logger.info(String.valueOf(layer.getHeight()));
//                logger.info(String.valueOf(layer.getWidth()));
            }
            logger.info("--");

            for (List<Double> predicts : layer.getPredicts().values()) {
                logger.info(String.valueOf(predicts.size()));
                predicts.forEach(predict -> logger.info(String.valueOf(predict)));
            }
            logger.info("--");
        }
    }
}

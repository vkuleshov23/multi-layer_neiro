package ai.service;

import ai.event.LearnEvent;
import ai.model.PerceptronOld;
import ai.util.Consts;
import ai.util.Randomizer;
import javafx.scene.image.Image;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class NeiroLearning {

    Logger logger = LoggerFactory.getLogger(NeiroLearning.class);

    private final ImageService imageService;

    private final FileController fileController;

    public double learnPercent(NeiroOld neiroOld) throws Exception {
        int count = 0;
        int successPredict = 0;
        for (String symbol : Consts.symbols) {
            for (String imageName : fileController.getTestFileNames(symbol)) {
                try {
                    Image image = fileController.loadImage(new File(imageName));
                    int[][] bitMap = imageService.imageForNeiro(image, symbol);
                    Pair<String, Double> predicts =
                            neiroOld.getPredicts(bitMap, imageService.getPixelSum(bitMap)).get(0);
                    if (predicts.getKey().equals(symbol)) {
                        successPredict++;
                    }
                    logger.info(predicts.getKey() + " : " + symbol);
                    count++;
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                }
            }
        }
        return  ((double)(successPredict)/(double)(count));
    }

    @EventListener(LearnEvent.class)
    public void learn(LearnEvent learnEvent) {
        NeiroOld neiroOld = learnEvent.getNeiroOld();
        for (PerceptronOld perceptronOld : neiroOld.getPerceptronOlds()) {
//            new Thread(() -> perceptronLearnCycle(neiro, perceptron)).start();
            perceptronLearnCycle(neiroOld, perceptronOld);
        }
    }

    public void perceptronLearnCycle(NeiroOld neiroOld, PerceptronOld perceptronOld) {
//        logger.info("Start thread for perceptron " + perceptron.getPerceptronSymbol());
        for (int i = 0; i < Consts.selectionSize; i++) {
            learnPerceptron(neiroOld, perceptronOld);
        }
//        logger.info("End thread for perceptron " + perceptron.getPerceptronSymbol());
    }

    public void learnPerceptron(NeiroOld neiroOld, PerceptronOld perceptronOld) {
        try {
            String patternSymbol = Randomizer.randSymbol();
            String imageName = imageService.getRandomImage(patternSymbol);
            learnForImage(neiroOld, perceptronOld, imageName, patternSymbol);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void learnForImage(NeiroOld neiroOld, PerceptronOld perceptronOld, String imageName, String patternSymbol) {
        try {
            Image image = fileController.loadImage(new File(imageName));
            int[][] bitMap = imageService.imageForNeiro(image, perceptronOld.getPerceptronSymbol());
            neiroOld.learn(perceptronOld, bitMap, imageService.getPixelSum(bitMap), patternSymbol);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}

package ai.service;

import ai.event.LearnEvent;
import ai.event.OldLearnEvent;
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

    public void learn(Neiro neiro, int pos) {
        learnCycle(neiro, pos);
    }

    public void learnCycle(Neiro neiro, int pos) {
        int epoch = (int)(Consts.selectionSize * (double)pos/2);
        for (int i = 0; i < epoch; i++) {
            learnOneImage(neiro);
        }
    }

    public void learnOneImage(Neiro neiro) {
        try {
            String patternSymbol = Randomizer.randSymbol();
            String imageName = imageService.getRandomImage(patternSymbol);
            learnForImage(neiro, imageName, patternSymbol);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void learnForImage(Neiro neiro, String imageName, String patternSymbol) {
        try {
            Image image = fileController.loadImage(new File(imageName));
            int[][] bitMap = imageService.imageForNeiro(image);
            neiro.learn(bitMap, imageService.getPixelSum(bitMap), patternSymbol);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}

package ai.controller;

import ai.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

import java.util.List;


@FxmlView
@Controller
@RequiredArgsConstructor
public class MainSceneController {

    @FXML
    private LineChart<Integer, Double> chart;
    @FXML
    private TextArea resPane;
    @FXML
    private TextField symbol;
    @FXML
    private Canvas canvas;
    private final NeiroOld neiroOld;

    private final Neiro neiro;
    private final WritingPixels writingPixels;
    private final ImageService imageService;

    private final NeiroLearning neiroLearning;

    private final ApplicationEventPublisher eventPublisher;

    private int pos = 1;

    Logger logger = LoggerFactory.getLogger(MainSceneController.class);

    @FXML
    public void initialize() {
        writingPixels.setCanvas(canvas);
        writingPixels.clear();
        ChartUpdater.init(chart);
    }

    public void clear(ActionEvent event) {
        writingPixels.clear();
        logger.info("CLEARED");
    }

    public void save(ActionEvent actionEvent) {
        imageService.setSymbol(symbol.getText());
        try {
            imageService.saveImage(canvas.snapshot(null, null));
            logger.info("SAVED");
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        writingPixels.clear();
    }

    public void recognize(ActionEvent actionEvent) {
        try {
            predict(getBitMap());
            logger.info("RECOGNIZED");
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void punish(ActionEvent actionEvent) {
        try {
            int[][] bitMap = getBitMap();
            double referenceSum = imageService.getPixelSum(bitMap);
            neiro.learn(bitMap, referenceSum, symbol.getText());
            predict(bitMap);
//            ChartUpdater.updateChart();
            logger.info("PUNISHED");
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    private  void predict(int[][] bitMap) {
        double referenceSum = imageService.getPixelSum(bitMap);
        List<Pair<String, Double>> predicts = neiro.predict(bitMap, referenceSum);
        resPane.clear();
        predicts.forEach(p -> resPane.appendText(p.getKey() + " = " + String.format("%.5f",p.getValue()) + "\n"));
    }

    private int[][] getBitMap() throws Exception {
        Image image = canvas.snapshot(null, null);
        return imageService.imageForNeiro(image);
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        writingPixels.printPixel(mouseEvent);
    }

    public void learn(ActionEvent actionEvent) throws Exception {
        this.neiroLearning.learn(this.neiro, pos++);
        ChartUpdater.printLossDot();
        ChartUpdater.printAccDot();
        logger.info("LEARNED");
    }

    public void test(ActionEvent actionEvent) {
        try {
            logger.info("LEARN PERCENT: " + neiroLearning.learnPercent(neiroOld));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void clearChart(ActionEvent actionEvent) {
        ChartUpdater.clear();
    }
}

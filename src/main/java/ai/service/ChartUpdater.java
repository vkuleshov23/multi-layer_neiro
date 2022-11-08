package ai.service;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class ChartUpdater {

    private static LineChart<Integer, Double> chart;
    private static final XYChart.Series<Integer, Double> lossSeries = new XYChart.Series<>();
    private static final ConcurrentLinkedQueue<Double> lossDataList = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Double> epochLossDataList = new ConcurrentLinkedQueue<>();
    private static Integer lossPos = 1;

    private static final XYChart.Series<Integer, Double> accSeries = new XYChart.Series<>();
    private static final ConcurrentLinkedQueue<Double> accDataList = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Double> epochAccDataList = new ConcurrentLinkedQueue<>();
    private static Integer accPos = 1;

    public static void init(LineChart<Integer, Double> charta) {
        chart = charta;
        chart.getData().add(lossSeries);
        chart.getData().add(accSeries);
    }

    @Scheduled(cron = "0/1 * * * * *")
    public static void updateLossChart() {
        if(lossDataList.size() > 0) {
            pickLossDot(lossDataList.remove());
        }
    }

    @Scheduled(cron = "0/1 * * * * *")
    public static void updateAccChart() {
        if(accDataList.size() > 0) {
            pickAccDot(accDataList.remove());
        }
    }

    public static void clear() {
        lossDataList.clear();
        lossPos = 1;
        lossSeries.getData().clear();
        lossSeries.getData().add(new XYChart.Data<>(-1, 0.0));
    }

    public static void pickLossDot(Double data) {
        if(lossSeries.getData().size() >= 50) {
            lossSeries.getData().remove(0);
            lossSeries.getData().forEach(s -> s.setXValue(s.getXValue()-1));
            lossPos--;
        }
        lossSeries.getData().add(new XYChart.Data<>(lossPos++, data));
    }

    public static void printLossDot() {
        lossDataList.add(epochLossDataList.stream().mapToDouble(d -> d).sum() / epochLossDataList.size());
        epochLossDataList.clear();
    }

    public static void addLossData(Double data) {
        epochLossDataList.add(data);
    }


    public static void pickAccDot(Double data) {
        if(accSeries.getData().size() >= 50) {
            accSeries.getData().remove(0);
            accSeries.getData().forEach(s -> s.setXValue(s.getXValue()-1));
            accPos--;
        }
        accSeries.getData().add(new XYChart.Data<>(accPos++, data));
    }

    public static void printAccDot() {
        accDataList.add(epochAccDataList.stream().mapToDouble(d -> d).sum() / epochAccDataList.size());
        epochAccDataList.clear();
    }

    public static void addAccData(Double data) {
        epochAccDataList.add(data);
    }

}

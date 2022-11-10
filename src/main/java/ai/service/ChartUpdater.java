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

    private static final XYChart.Series<Integer, Double> precisionSeries = new XYChart.Series<>();
    private static final ConcurrentLinkedQueue<Double> precisionDataList = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Double> epochPrecisionDataList = new ConcurrentLinkedQueue<>();
    private static Integer precisionPos = 1;

    private static final XYChart.Series<Integer, Double> recallSeries = new XYChart.Series<>();
    private static final ConcurrentLinkedQueue<Double> recallDataList = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Double> epochRecallDataList = new ConcurrentLinkedQueue<>();
    private static Integer recallPos = 1;

    private static final XYChart.Series<Integer, Double> f1Series = new XYChart.Series<>();
    private static final ConcurrentLinkedQueue<Double> f1DataList = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Double> epochF1DataList = new ConcurrentLinkedQueue<>();
    private static Integer f1Pos = 1;

    private static final XYChart.Series<Integer, Double> accSeries = new XYChart.Series<>();
    private static final ConcurrentLinkedQueue<Double> accDataList = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Double> epochAccDataList = new ConcurrentLinkedQueue<>();
    private static Integer accPos = 1;

    public static void init(LineChart<Integer, Double> charta) {
        chart = charta;

        lossSeries.setName("Loss");
        chart.getData().add(lossSeries);

        accSeries.setName("Accuracy");
        chart.getData().add(accSeries);

        precisionSeries.setName("Precision");
        chart.getData().add(precisionSeries);

        recallSeries.setName("Recall");
        chart.getData().add(recallSeries);

        f1Series.setName("F1 Score");
        chart.getData().add(f1Series);

    }

    @Scheduled(cron = "0/1 * * * * *")
    public static void updateLossChart() {
        if(lossDataList.size() > 0) {
            pickLossDot(lossDataList.remove());
        }
        if(accDataList.size() > 0) {
            pickAccDot(accDataList.remove());
        }
        if(precisionDataList.size() > 0) {
            pickPrecisionDot(precisionDataList.remove());
        }
        if(recallDataList.size() > 0) {
            pickRecallDot(recallDataList.remove());
        }
        if(f1DataList.size() > 0) {
            pickF1Dot(f1DataList.remove());
        }
    }

    public static void clear() {
        lossDataList.clear();
        lossPos = 1;
        lossSeries.getData().clear();
        lossSeries.getData().add(new XYChart.Data<>(-1, 0.0));

        accDataList.clear();
        accPos = 1;
        accSeries.getData().clear();
        accSeries.getData().add(new XYChart.Data<>(-1,0.0));

        precisionDataList.clear();
        precisionPos = 1;
        precisionSeries.getData().clear();
        precisionSeries.getData().add(new XYChart.Data<>(-1,0.0));

        recallDataList.clear();
        recallPos = 1;
        recallSeries.getData().clear();
        recallSeries.getData().add(new XYChart.Data<>(-1,0.0));

        f1DataList.clear();
        f1Pos = 1;
        f1Series.getData().clear();
        f1Series.getData().add(new XYChart.Data<>(-1,0.0));
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

    public static void pickPrecisionDot(Double data) {
        if(precisionSeries.getData().size() >= 50) {
            precisionSeries.getData().remove(0);
            precisionSeries.getData().forEach(s -> s.setXValue(s.getXValue()-1));
            precisionPos--;
        }
        precisionSeries.getData().add(new XYChart.Data<>(precisionPos++, data));
    }

    public static void printPrecisionDot() {
        precisionDataList.add(epochPrecisionDataList.stream().mapToDouble(d -> d).sum() / epochPrecisionDataList.size());
        epochPrecisionDataList.clear();
    }

    public static void addPrecisionData(Double data) {
        epochPrecisionDataList.add(data);
    }

    public static void pickRecallDot(Double data) {
        if(recallSeries.getData().size() >= 50) {
            recallSeries.getData().remove(0);
            recallSeries.getData().forEach(s -> s.setXValue(s.getXValue()-1));
            recallPos--;
        }
        recallSeries.getData().add(new XYChart.Data<>(recallPos++, data));
    }

    public static void printRecallDot() {
        recallDataList.add(epochRecallDataList.stream().mapToDouble(d -> d).sum() / epochRecallDataList.size());
        epochRecallDataList.clear();
    }

    public static void addRecallData(Double data) {
        epochRecallDataList.add(data);
    }

    public static void pickF1Dot(Double data) {
        if(f1Series.getData().size() >= 50) {
            f1Series.getData().remove(0);
            f1Series.getData().forEach(s -> s.setXValue(s.getXValue()-1));
            f1Pos--;
        }
        f1Series.getData().add(new XYChart.Data<>(f1Pos++, data));
    }

    public static void printF1Dot() {
        f1DataList.add(epochF1DataList.stream().mapToDouble(d -> d).sum() / epochF1DataList.size());
        epochF1DataList.clear();
    }

    public static void addF1Data(Double data) {
        epochF1DataList.add(data);
    }

}

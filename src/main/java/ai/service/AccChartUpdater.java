package ai.service;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class AccChartUpdater {

    private static LineChart<Integer, Double> chart;
    private static final XYChart.Series<Integer, Double> series = new XYChart.Series<>();
    private static Integer pos = 1;

    private static final ConcurrentLinkedQueue<Double> dataList = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Double> epochDataList = new ConcurrentLinkedQueue<>();

    public static void init(LineChart<Integer, Double> charta) {
        chart = charta;
        chart.getData().add(series);
    }

    @Scheduled(cron = "0/1 * * * * *")
    public static void updateChart() {
        if(dataList.size() > 0) {
            pickDot(dataList.remove());
        }
    }

    public static void clear() {
        dataList.clear();
        pos = 1;
        series.getData().clear();
        series.getData().add(new XYChart.Data<>(-1, 0.0));
    }

    public static void pickDot(Double data) {
        if(series.getData().size() >= 50) {
            series.getData().remove(0);
            series.getData().forEach(s -> s.setXValue(s.getXValue()-1));
            pos--;
        }
        series.getData().add(new XYChart.Data<>(pos++, data));
    }

    public static void print() {
        dataList.add(epochDataList.stream().mapToDouble(d -> d).sum() / epochDataList.size());
        epochDataList.clear();
    }

    public static void addData(Double data) {
        epochDataList.add(data);
    }

}

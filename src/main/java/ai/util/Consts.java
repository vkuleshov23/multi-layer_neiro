package ai.util;

public class Consts {

    public static final Integer layersCount = 3;

    public static final Integer selectionSize = 1_000;

    public static final Integer xSize = 32;

    public static final Integer ySize = 32;

    public static final double maxWeight = 1.0;

    public static final double minWeight = -1.0;

    public static final double maxNewWeight = 0.5;

    public static final double minNewWeight = -0.5;

    public static double step = 0.01;

    public static double rate = 0.2;

    public static double wrong_threshold = 0.0;

    public static double recognize_threshold = 1.0;

    public static final String[] symbols = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static final String fileSystemDelimiter = "\\";

    public static final String basicWay = "C:\\Users\\Вадим\\Desktop\\Study\\7 семестр\\AI_CS\\multi-layer_neiro\\";

    public static final String neiroFile = basicWay + "neiro.ser";

    public static final String dataset = basicWay + "DataSet" + fileSystemDelimiter;

    public static final String testset = basicWay + "TestSet" + fileSystemDelimiter;

    public static final String cache = basicWay + "Cache" + fileSystemDelimiter;

}

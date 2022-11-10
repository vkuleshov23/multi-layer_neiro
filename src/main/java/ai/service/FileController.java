package ai.service;

import ai.util.Consts;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Objects;

@Service
public class FileController {

    Logger logger = LoggerFactory.getLogger(FileController.class);




    public NeiroOld loadNeiroOld() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Consts.neiroOldFile));
        return (NeiroOld) ois.readObject();
    }

    public void saveNeiroOld(NeiroOld neiroOld) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Consts.neiroOldFile));) {
            oos.writeObject(neiroOld);
        } catch (IOException e) {
           logger.error(e.getMessage());
        }
    }

    public Neiro loadNeiro() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Consts.neiroFile));
        return (Neiro) ois.readObject();
    }

    public void saveNeiro(Neiro neiro) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Consts.neiroFile));) {
            oos.writeObject(neiro);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String[] getFileNames(String dirName) throws Exception {
        return Arrays.stream(Objects.requireNonNull(new File(Consts.dataset + dirName).list()))
                .map(name -> (Consts.dataset + dirName + Consts.fileSystemDelimiter + name)).toArray(String[]::new);
    }

    public String[] getTestFileNames(String dirName) {
        return Arrays.stream(Objects.requireNonNull(new File(Consts.testset + dirName).list()))
                .map(name -> (Consts.testset + dirName + Consts.fileSystemDelimiter + name)).toArray(String[]::new);
    }

    public Image loadImage(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        return SwingFXUtils.toFXImage(image, null);
    }

    public Image prepareImageFoNeiro(Image image) throws Exception {
        File file = new File(Consts.cache + "cache.png");
        return save(file, image);
    }

    public Image prepareImageFoNeiro(Image image, String symbol) throws Exception {
        File file = new File(Consts.cache + "cache" + symbol + ".png");
        return save(file, image);
    }

    public void saveImage(Image image, String ... fileWaySlices) throws Exception {
        String fileWay = toOneWay(fileWaySlices);
        File file = new File(Consts.dataset + fileWay + filesCount(fileWay) + ".png");
        save(file, image);
    }


    private String toOneWay(String ... fileWay) {
        StringBuilder oneWay = new StringBuilder();
        for(String slice : fileWay){
            oneWay.append(slice).append(Consts.fileSystemDelimiter);
        }
        return oneWay.toString();
    }


    public int filesCount(String fileWay) throws Exception {
        try {
            return Objects.requireNonNull(new File(Consts.dataset + fileWay).list()).length;
        } catch (Exception e) {
            if (new File(Consts.dataset + fileWay).mkdirs()) {
                return 0;
            }
        }
        throw new Exception("Can't create directory");
    }

    public Image save(File file, Image image) throws Exception {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            bufferedImage = trim(bufferedImage);
            ImageIO.write(bufferedImage, "png", file);
            BufferedImage readBufferedImage = ImageIO.read(file);
            Image readeImage = toPerceptronResolution(SwingFXUtils.toFXImage(readBufferedImage, null));
            readBufferedImage = SwingFXUtils.fromFXImage(readeImage, null);
            ImageIO.write(readBufferedImage, "png", file);
            return readeImage;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    private Image toPerceptronResolution(Image image) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(Consts.xSize);
        imageView.setFitWidth(Consts.ySize);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        return convertToBinary(imageView.snapshot(null, null));
    }

    private Image convertToBinary(Image image) {
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        PixelReader pixelReader = writableImage.getPixelReader();
        for (int i = 0; i < writableImage.getHeight(); i++) {
            for (int j = 0; j < writableImage.getWidth(); j++) {
                Color c = pixelReader.getColor(j, i);
                if (c.getRed() > 0 || c.getGreen() > 0 || c.getBlue() > 0) {
                    pixelWriter.setColor(j, i, Color.WHITE);
                } else {
                    pixelWriter.setColor(j, i, Color.BLACK);
                }
            }
        }
        return new ImageView(writableImage).snapshot(null, null);
    }

    private BufferedImage trim(BufferedImage img) {
        int top = getTopInset(img);
        int bottom = getBottomInset(img);
        int left = getLeftInset(img);
        int right = getRightInset(img);
        return img.getSubimage(left, top, right-left, bottom-top);
    }

    private int getLeftInset(BufferedImage img) {
        for(int x = 0; x < img.getHeight(); x++) {
            for(int y = 0; y < img.getWidth(); y++) {
                if (isNotWhite(img, x, y)) {
                    return x;
                }
            }
        }
        return 0;
    }

    private int getRightInset(BufferedImage img) {
        for(int x = img.getHeight()-1; x >= 0 ; x--) {
            for(int y = 0; y < img.getWidth(); y++) {
                if (isNotWhite(img, x, y)) {
                    return x;
                }
            }
        }
        return img.getHeight()-1;
    }

    private int getTopInset(BufferedImage img) {
        for(int y = 0; y < img.getWidth(); y++) {
            for(int x = 0; x < img.getHeight(); x++) {
                if (isNotWhite(img, x, y)) {
                    return y;
                }
            }
        }
        return 0;
    }

    private int getBottomInset(BufferedImage img) {
        for(int y = img.getWidth()-1; y >= 0; y--) {
            for(int x = 0; x < img.getHeight(); x++) {
                if (isNotWhite(img, x, y)) {
                    return y;
                }
            }
        }
        return 0;
    }

    private boolean isNotWhite(BufferedImage img, int x, int y) {
        return !new java.awt.Color(img.getRGB(x, y), true).equals(java.awt.Color.WHITE);
    }
}

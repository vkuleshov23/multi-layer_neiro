package ai.service;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Service;

@Service
public class WritingPixels {

    private PixelWriter pixelWriter;
    private static final int pixelSize = 12;
    private int xPos, yPos;
    private int xSize, ySize;

    public PixelWriter setCanvas(Canvas  canvas) {
        xSize = (int) canvas.getHeight();
        ySize = (int) canvas.getWidth();
        return this.pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
    }


    public void printPixel(MouseEvent e) {
        CrdToInt(e);
        for (int x = 0; x < pixelSize; x++) {
            for (int y = 0; y < pixelSize; y++) {
                pixelWriter.setColor(xPos + x, yPos + y, Color.BLACK);
            }
        }
    }


    public void clear(){
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                pixelWriter.setColor(x, y, Color.WHITE);
            }
        }
    }


    private void CrdToInt(MouseEvent e) {
        this.xPos = (int)(e.getX());
        this.yPos = (int)(e.getY());
    }


}

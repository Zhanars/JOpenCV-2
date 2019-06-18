import com.sun.javafx.geom.Vec3d;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

public class PageMaker {
    public static Mat src = new Mat();
    public static void Make(){
        String imgSrc = "images/PDFtoJPG.me-1.jpg";
        src = Imgcodecs.imread(imgSrc);
        int depth = 70;
        for (int i = 0; i < depth; i++) {
            // 1 квадрат
            Imgproc.rectangle(src, new Point(i, i), new Point(depth - i, depth - i), new Scalar(0, 0));
            // 2 квадрат
            Imgproc.rectangle(src, new Point(src.width() - depth + i, i), new Point(src.width() - i, depth - i), new Scalar(0, 0));
            // 3 квадрат
            //Imgproc.rectangle(src, new Point((src.width() - depth)/2 + i, i), new Point((src.width() + depth)/2, depth), new Scalar(0, 0));
            // 4 квадрат
            Imgproc.rectangle(src, new Point(i,src.height() - depth + i), new Point( depth - i, src.height() - i), new Scalar(0, 0));
            // 5 квадрат
            Imgproc.rectangle(src, new Point(src.width() - depth + i, src.height() - depth + i), new Point(src.width() - i, src.height() - i), new Scalar(0, 0));
        }
        //predmet1(30, 8, 0);
        //Imgproc.circle(src, new Point(415,545), 50, new Scalar(0, 0, 255));
        for (int i = 0; i < 3; i++) {
            //System.out.println(src.get(100, 100)[i]);
        }
        Imgcodecs.imwrite("images/out1.jpg", src);
        System.out.println("File ready");
    }

    public Image toBufferedImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
    public static void predmet1(int countAnswer, int countVariant, int startX){
        int depth = 2;
        int Pixels = 38;
        int startY = 1155;
        int endY = startY + (countAnswer + 1) * Pixels + depth * 3;
        int endX = startX + (countVariant + 1) * Pixels + depth * 3;
        for (int i = 0; i < depth * 2; i++) {
            Imgproc.rectangle(src, new Point(startX, startY), new Point(endX, endY), new Scalar(0, 0));
            startX ++;
            startY ++;
            endX --;
            endY --;
        }
        int newStartY = startY;
        for (int i = 0; i < countAnswer; i++) {
            System.out.println(startX + " " + newStartY + " " + endX + " " + (newStartY + 1));
            newStartY += (Pixels - depth);
            Imgproc.rectangle(src, new Point(startX, newStartY), new Point(endX, newStartY + 1), new Scalar(0, 0));
            newStartY += depth;
        }
        int newStartX = startX;
        startY += (Pixels - depth);
        for (int i = 0; i < countVariant; i++) {
            newStartX += (Pixels - depth);
            Imgproc.rectangle(src, new Point(newStartX, startY), new Point(newStartX + 1, endY), new Scalar(0, 0));
            newStartX += depth;
        }
    }
}

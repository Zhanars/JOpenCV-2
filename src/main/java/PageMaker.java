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

public class PageMaker {
    public static void Make(){
        String imgSrc = "images/source.png";
        Mat src = Imgcodecs.imread(imgSrc);
        for (int i = 0; i < 30; i++) {
            // 1 квадрат
            Imgproc.rectangle(src, new Point(i, i), new Point(30, 30), new Scalar(0, 0));
            // 2 квадрат
            Imgproc.rectangle(src, new Point(src.width() - 30 + i, i), new Point(src.width(), 30), new Scalar(0, 0));
            // 3 квадрат
            Imgproc.rectangle(src, new Point(src.width()/2 - 15 + i, i), new Point(src.width()/2 + 15, 30), new Scalar(0, 0));
            // 4 квадрат
            Imgproc.rectangle(src, new Point(i,src.height() - 30 + i), new Point( 30, src.height()), new Scalar(0, 0));
            // 5 квадрат
            Imgproc.rectangle(src, new Point(src.width() - 30 + i, src.height() - 30 + i), new Point(src.width(), src.height()), new Scalar(0, 0));
        }
        Imgproc.circle(src, new Point(415,545), 50, new Scalar(0, 0, 255));
        for (int i = 0; i < 3; i++) {
            System.out.println(src.get(100, 100)[i]);
        }
        Imgcodecs.imwrite("images/out.png", src);
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


}

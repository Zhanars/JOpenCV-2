import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class PageScanner {
    public static void rotate(){
        String imgSrc = "images/out1.jpg";
        Mat img1 = Imgcodecs.imread(imgSrc, 1);
        Mat src = Imgcodecs.imread(imgSrc);
        System.out.println(src.width());
        System.out.println(src.height());
    }
}

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class PageScanner {
    public static void rotate(String res){
        String imgSrc = res;
        Mat src = Imgcodecs.imread(imgSrc);
        System.out.println(src.width());
        System.out.println(src.height());
    }
}

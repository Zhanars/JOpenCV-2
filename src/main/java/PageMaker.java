import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

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

        Imgcodecs.imwrite("images/out.png", src);
        System.out.println("File ready");
    }


}

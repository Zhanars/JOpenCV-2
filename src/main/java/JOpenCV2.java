import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class JOpenCV2 {
    public static void main(String[] args) {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        PageMaker.Make();
        PageScanner.rotate();
    }
}

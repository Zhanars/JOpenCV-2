import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;

public class PageScanner {
    public static BufferedImage gridImage;
    public static WritableRaster raster;
    public static int[] topleft = new int[2];
    public static int[] topright = new int[2];
    public static int[] botleft = new int[2];
    public static int[] botright = new int[2];
    public static int[] ellipse = new int[2];
    public static void rotate(String res) throws IOException {
        File file = new File(res);
        BufferedImage bufferedImage = ImageIO.read(file);
        BufferedImage image = new BufferedImage( bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
        Graphics g = image.getGraphics();
        g.drawImage( bufferedImage, 0, 0, null );
        g.dispose();
        RescaleOp rescaleOp = new RescaleOp(1.4f, 15, null);
        rescaleOp.filter(image, image);
        int mintl = 1000000, mintr = 1000000, minbl = 1000000, minbr = 1000000, minell = 100000, width = image.getWidth() - 23, height = image.getHeight() - 23;
        raster = image.getRaster();
        for (int x = 0; x < 150; x++){
            for (int y = 0; y < 150; y++){
                if (mintl > checkRectanle(x, y)){
                    mintl = checkRectanle(x, y);
                    topleft[0] = x;
                    topleft[1] = y;
                }
                if (mintr > checkRectanle(width - x, y)){
                    mintr = checkRectanle(width - x, y);
                    topright[0] = width - x + 21;
                    topright[1] = y;
                }
                if (minbl > checkRectanle(x, height - y)){
                    minbl = checkRectanle(x, height - y);
                    botleft[0] = x;
                    botleft[1] = height - y + 21;
                }
                if (minbr > checkRectanle(width - x, height - y)){
                    minbr = checkRectanle(width - x, height - y);
                    botright[0] = width - x + 21;
                    botright[1] = height - y + 21;
                }
            }
        }
        if (
                (Math.abs(topleft[0] - botleft[0]) > 10) ||
                (Math.abs(topleft[1] - topright[1]) > 10) ||
                (Math.abs(topright[0] - botright[0]) > 10) ||
                (Math.abs(botleft[1] - botright[1]) > 10)) {
            System.out.print("Ошибка сканирования");
        }
        int ellipsePos = 0;
        for (int x = 22; x < 150; x++){
            for (int y = 22; y < 150; y++){
                if (minell > checkEllipse(topleft[0] + x,topleft[1] + y)){
                    minell = checkEllipse(topleft[0] + x,topleft[1] + y);
                    ellipse[0] = topleft[0] + x;
                    ellipse[1] = topleft[1] + y;
                    ellipsePos = 1;
                }
                if (minell > checkEllipse(topright[0] - x,topright[1] + y)){
                    minell = checkEllipse(topright[0] - x,topright[1] + y);
                    ellipse[0] = topright[0] - x;
                    ellipse[1] = topright[1] + y;
                    ellipsePos = 0;
                }
                if (minell > checkEllipse(botleft[0] + x,botleft[1] - y)){
                    minell = checkEllipse(botleft[0] + x,botleft[1] - y);
                    ellipse[0] = botleft[0] + x;
                    ellipse[1] = botleft[1] - y;
                    ellipsePos = 2;
                }
                if (minell > checkEllipse(botright[0] - x,botright[1] - y)){
                    minell = checkEllipse(botright[0] - x,botright[1] - y);
                    ellipse[0] = botright[0] - x;
                    ellipse[1] = botright[1] - y;
                    ellipsePos = 3;
                }
            }
        }
        int[] pixel = new int[1];
        pixel[0] = 255;
        raster.setPixel(topleft[0], topleft[1], pixel);
        raster.setPixel(topright[0], topright[1], pixel);
        raster.setPixel(botleft[0], botleft[1], pixel);
        raster.setPixel(botright[0], botright[1], pixel);
        raster.setPixel(ellipse[0], ellipse[1], pixel);
        image.setData(raster);
        gridImage = image;
        for (int i = 0; i < ellipsePos; i++){
            System.out.println("1 " + topleft[0] + ", " + topleft[1]);
            System.out.println("2 " + topright[0] + ", " + topright[1]);
            System.out.println("3 " + botleft[0] + ", " + botleft[1]);
            System.out.println("4 " + botright[0] + ", " + botright[1]);
            System.out.println("- - - - - ");
            gridImage = rotateImag(gridImage);
        }
        System.out.println("1 " + topleft[0] + ", " + topleft[1]);
        System.out.println("2 " + topright[0] + ", " + topright[1]);
        System.out.println("3 " + botleft[0] + ", " + botleft[1]);
        System.out.println("4 " + botright[0] + ", " + botright[1]);
        System.out.println(ellipsePos + ", " + ellipse[0] + ", " + ellipse[1]);
        ImageIO.write(gridImage, "jpg", new File("123.jpg"));
        System.out.println("Finish print");
    }
    public static void scan(){
        Imaging imaging = new Imaging("myApp", 0);
        Result result = null;
        try {
            result = imaging.scan(Request.fromJson(
                    "{ \"twain_cap_setting\" : {\n" +
                            "    \"ICAP_PIXELTYPE\" : \"TWPT_GRAY\"," +
                            "    \"ICAP_XRESOLUTION\" : \"50\", " +
                            "    \"ICAP_YRESOLUTION\" : \"50\"," +
                            "    \"ICAP_BITDEPTH\" : \"8\"," +
                            "    \"ICAP_SUPPORTEDSIZES\" : \"TWSS_A4\" " +
                            "  },"

                            + "\"output_settings\" : [ {"
                            + "  \"type\" : \"save\","
                            + "  \"format\" : \"jpg\","
                            + "  \"save_path\" : \"${TMP}\\\\${TMS}${EXT}\""
                            + "} ]"
                            + "}"), "WIA-HP LJ M129M134 (USB)", false, false);


            System.out.println(result == null ? "(null)" : result.toJson(true));
            JSONObject object = new JSONObject(result.toJson(true));
            JSONArray getArray = object.getJSONArray("output");
            String res = "";
            for (int i = 0; i < getArray.length(); i++) {
                JSONObject obj = getArray.getJSONObject(i);
                res = obj.get("result").toString();
                res = res.substring(2, res.length() - 2);
                //res = res.replace("\\\\", "/");
            }
            System.out.println(res);
            nu.pattern.OpenCV.loadShared();
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            rotate(res);
        }catch (IOException e) {
                System.out.println("Ошибка сканирования");
            e.printStackTrace();
        }
    }
    public static int checkRectanle(int x, int y) {
        int sum = 0;
        for (int i = x; i < x + 21; i++){
            for (int j = y; j < y + 21; j++){
                int[] pixel = raster.getPixel(i, j, new int[1]);
                sum += pixel[0];
            }
        }
        return sum;
    }
    public static int checkEllipse(int x, int y) {
        int b = 14;
        int sum = 0;
        for (int i = x - b; i < x + b; i++){
            for (int j = y - b; j < y + b; j++){
                if (((i - x)*(i - x) + (j - y)*(j - y)) < (b*b)){
                    int[] pixel = raster.getPixel(i, j, new int[1]);
                    sum += pixel[0];
                }
            }
        }
        return sum;
    }
    public static BufferedImage rotateImag(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage dest = new BufferedImage(height, width, src.getType());
        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);
        int[] buf = topleft;
        topleft[1] = botleft[0];
        topleft[0] = height - botleft[1];

        botleft[1] = botright[0];
        botleft[0] = height - botright[1];

        botright[1] = topright[0];
        botright[0] = height - topright[1];

        topright[1] = buf[0];
        topright[0] = width - buf[1];
        return dest;
    }

}

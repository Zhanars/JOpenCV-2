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
    public static int minellipce = 110000;
    public static String error = "";
    public static void rotate(String res) throws IOException {
        error = "";
        File file = new File(res);
        BufferedImage bufferedImage = ImageIO.read(file);
        BufferedImage image = new BufferedImage( bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
        Graphics g = image.getGraphics();
        g.drawImage( bufferedImage, 0, 0, null );
        g.dispose();
        RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
        rescaleOp.filter(image, image);
        int mintl = 1000000, mintr = 1000000, minbl = 1000000, minbr = 1000000, minell = 100000, width = image.getWidth() - 23, height = image.getHeight() - 23;
        raster = image.getRaster();
        for (int x = 20; x < 150; x++){
            for (int y = 20; y < 150; y++){
                if (mintl > checkRectangle(x, y)){
                    mintl = checkRectangle(x, y);
                    topleft[0] = x;
                    topleft[1] = y;
                }
                if (mintr > checkRectangle(width - x, y)){
                    mintr = checkRectangle(width - x, y);
                    topright[0] = width - x + 21;
                    topright[1] = y;
                }
                if (minbl > checkRectangle(x, height - y)){
                    minbl = checkRectangle(x, height - y);
                    botleft[0] = x;
                    botleft[1] = height - y + 21;
                }
                if (minbr > checkRectangle(width - x, height - y)){
                    minbr = checkRectangle(width - x, height - y);
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
            System.out.println("Ошибка сканирования");
            return;
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
        image.setData(raster);
        gridImage = image;
        for (int i = 0; i < ellipsePos; i++){
            gridImage = rotateImag(gridImage);
        }
        raster = gridImage.getRaster();
        //rectangle(topleft, 20);
        //rectangle(topright, 20);
        //rectangle(botleft, 20);
        //rectangle(botright, 20);
        System.out.println("Finish print");
        System.out.println(getSection1());
        System.out.println(getSection2());
        System.out.println(getSection3());
        System.out.println(getSection5());
        System.out.println(getSection6());
        String section7 = getSection7891(1.5, 8) + getSection7892(8.5, 10);
        System.out.println("Section7 " + section7.length() + ":" + section7);
        String section8 = getSection7891(15, 9) + getSection7892(22, 10);
        System.out.println("Section8 " + section8.length() + ":" + section8);
        String section9 = getSection7891(28.5, 12) + getSection7892(35.5, 14);
        System.out.println("Section9 " + section9.length() + ":" + section9);
        gridImage.setData(raster);
        ImageIO.write(gridImage, "jpg", new File("123.jpg"));
        System.out.println("Error:" + error);
    }
    public static void scan(){
        Imaging imaging = new Imaging("myApp", 0);
        Result result = null;
        try {
            result = imaging.scan(
                    "{ \"twain_cap_setting\" : {" +
                            "    \"ICAP_PIXELTYPE\" : \"TWPT_GRAY\"," +
                            "    \"ICAP_XRESOLUTION\" : \"200\", " +
                            "    \"ICAP_YRESOLUTION\" : \"200\"," +
                            "    \"ICAP_BITDEPTH\" : \"8\"," +
                            "    \"ICAP_SUPPORTEDSIZES\" : \"TWSS_A4\" " +
                            "  },"
                            + "\"output_settings\" : [ {"
                            + "  \"type\" : \"save\","
                            + "  \"format\" : \"jpg\","
                            + "  \"save_path\" : \"${TMP}\\\\${TMS}${EXT}\""
                            + "} ]"
                            + "}", "select", false, false);

            JSONObject object = new JSONObject(result.toJson(true));
            JSONArray getArray = object.getJSONArray("output");
            JSONObject obj = getArray.getJSONObject(0);
            JSONArray resArray = obj.getJSONArray("result");
            System.out.println(result == null ? "(null)" : resArray.length());
            for (int i = 0; i < resArray.length(); i++){
                System.out.println(resArray.get(i));
                rotate(resArray.get(i).toString());
            }
        }catch (IOException e) {
                System.out.println("Ошибка сканирования");
            e.printStackTrace();
        }
    }
    public static String getSection1(){
        String res = "";
        int x = topleft[0] + (int)(38*0.5), y = topleft[1] + (int)(36*2.5);
        for (int i = 0; i < 14; i++){
            if (getColumn12(x,y).length() > 1) {
                error += "\nОшибка в Секторе 1 и Колонке " + (i + 1);
                res += " ";
            } else {
                res += getColumn12(x,y);
            }
            x += 38;
        }
        return res;
    }
    public static String getSection2(){
        String res = "";
        int x = topleft[0] + (int)(38*15.5), y = topleft[1] + (int)(36*2.5);
        for (int i = 0; i < 12; i++){
            if (getColumn12(x,y).length() > 1) {
                error += "\nОшибка в Секторе 2 и Колонке " + (i + 1);
                res += " ";
            } else {
                res += getColumn12(x,y);
            }
            x += 38;
        }
        return res;
    }
    public static String getSection3(){
        String res = "";
        int x = topleft[0] + (int)(38*28.5), y = topleft[1] + (int)(36*4.5) + 5;
        for (int i = 0; i < 12; i++){
            if (getColumn35(x,y).length() > 1) {
                error += "\nОшибка в Секторе 3 и Колонке " + (i + 1);
                res += " ";
            } else {
                res += getColumn35(x,y);
            }
            x += 38;
        }
        return res;
    }
    public static String getSection5(){
        String res = "";
        int x = topleft[0] + (int)(38*36.5) - 2, y = topleft[1] + (int)(36*33) + 5;
        for (int i = 0; i < 4; i++){
            if (getColumn35(x,y).length() > 1) {
                error += "\nОшибка в Секторе 5 и Колонке " + (i + 1);
                res += " ";
            } else {
                res += getColumn35(x,y);
            }
            x += 38;
        }
        return res;
    }
    public static String getSection6(){
        String result = "";
        int x = topleft[0] + (int)(38*34.5) - 2, y = topleft[1] + (int)(36*34) + 5;
        for (int i = 1; i < 5; i++){
            if (minellipce > checkEllipse(x, y)){
                result += getDisseplinName(i);
            }
            int[] XoY = new int[2];
            XoY[0] = x - 14;
            XoY[1] = y - 14;
            rectangle(XoY, 28);
            if (i % 2 == 0){
                y++;
            }
            y += 36;
        }
        if (result.length() > 10){
            error += "\nОшибка в Секторе 6";
            result = "";
        }
        return result;
    }
    public static String getSection7891(double d, int plusY){
        String res = "";
        int x = topleft[0] + (int)(38*d), y = topleft[1] + (int)(36*46) + plusY;
        for (int i = 1; i < 14; i++){
            if (getColumn789(x,y).length() > 1) {
                if (d < 10) {
                    error += "\nОшибка в Секторе 7 и вопрос " + i;
                } else if(d < 25){
                    error += "\nОшибка в Секторе 8 и вопрос " + i;
                } else if(d < 37){
                    error += "\nОшибка в Секторе 9 и вопрос " + i;
                }
                res += " ";
            } else {
                res += getColumn789(x,y);
            }
            if (i % 2 == 0){
                y++;
            }
            if (i % 7 == 0){
                y++;
            }
            y += 36;
        }
        return res;
    }
    public static String getSection7892(double d, int plusY){
        String res = "";
        int x = topleft[0] + (int)(38*d), y = topleft[1] + (int)(36*46) + plusY;
        for (int i = 14; i < 26; i++){
            if (getColumn789(x,y).length() > 1) {
                if (d < 10) {
                    error += "\nОшибка в Секторе 7 и вопрос " + i;
                } else if(d < 25){
                    error += "\nОшибка в Секторе 8 и вопрос " + i;
                } else if(d < 37){
                    error += "\nОшибка в Секторе 9 и вопрос " + i;
                }
                res += " ";
            } else {
                res += getColumn789(x,y);
            }
            if (i % 2 == 0){
                y++;
            }
            if (i % 7 == 0){
                y++;
            }
            y += 36;
        }
        return res;
    }
    public static int checkRectangle(int x, int y) {
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
        int x = topleft[0], y = topleft[1];
        topleft[1] = botleft[0];
        topleft[0] = height - botleft[1];

        botleft[1] = botright[0];
        botleft[0] = height - botright[1];

        botright[1] = topright[0];
        botright[0] = height - topright[1];

        topright[1] = x;
        topright[0] = height - y;
        return dest;
    }
    public static void rectangle(int[] a, int rec){
        int[] pixel = new int[1];
        pixel[0] = 0;
        //System.out.println("a[0]=" + a[0] + ", a[1]=" + a[1]);
        //System.out.println("- - - - - -");
        for (int i = 0; i <= rec; i++){
            if (i % 2 == 0){
                pixel[0] = 0;
            } else {
                pixel[0] = 255;
            }
            raster.setPixel(a[0] + i, a[1], pixel);
            raster.setPixel(a[0], a[1] + i, pixel);
            raster.setPixel(a[0] + i, a[1] + rec, pixel);
            raster.setPixel(a[0] + rec, a[1] + i, pixel);
        }
    }
    public static void show(int[] a){
        System.out.println("b[0]=" + a[0] + ", b[1]=" + a[1]);
        System.out.println("- - - - - -");
    }
    public static String getColumn12(int x, int y){
        String result = "";
        for (int i = 1; i < 42; i++){
            if (minellipce > checkEllipse(x, y)){
                result += getChar(i - 1);
            }
            int[] XoY = new int[2];
            XoY[0] = x - 14;
            XoY[1] = y - 14;
            rectangle(XoY, 28);
            if (i % 2 == 0){
                y++;
            }
            if (i % 7 == 0){
                y++;
            }
            y += 36;
        }
        return result;
    }
    public static String getColumn35(int x, int y){
        String result = "";
        for (int i = 1; i < 11; i++){
            if (minellipce > checkEllipse(x, y)){
                result += getNumber(i - 1);
            }
            int[] XoY = new int[2];
            XoY[0] = x - 14;
            XoY[1] = y - 14;
            rectangle(XoY, 28);
            if (i % 2 == 0){
                y++;
            }
            if (i % 7 == 0){
                y++;
            }
            y += 36;
        }
        return result;
    }
    public static String getColumn789(int x, int y){
        String result = "";
        for (int i = 1; i < 6; i++){
            if (minellipce > checkEllipse(x, y)){
                result += getABCDE(i - 1);
            }
            int[] XoY = new int[2];
            XoY[0] = x - 14;
            XoY[1] = y - 14;
            rectangle(XoY, 28);
            x += 38;
        }
        if (result == "") result = " ";
        return result;
    }
    public static char getChar(int i){
        switch (i){
            case 0: return 'А';
            case 1: return 'Ә';
            case 2: return 'Б';
            case 3: return 'В';
            case 4: return 'Г';
            case 5: return 'Ғ';
            case 6: return 'Д';
            case 7: return 'Е';
            case 8: return 'Ж';
            case 9: return 'З';
            case 10: return 'И';
            case 11: return 'Й';
            case 12: return 'К';
            case 13: return 'Қ';
            case 14: return 'Л';
            case 15: return 'М';
            case 16: return 'Н';
            case 17: return 'Ң';
            case 18: return 'О';
            case 19: return 'Ө';
            case 20: return 'П';
            case 21: return 'Р';
            case 22: return 'С';
            case 23: return 'Т';
            case 24: return 'У';
            case 25: return 'Ұ';
            case 26: return 'Ү';
            case 27: return 'Ф';
            case 28: return 'Х';
            case 29: return 'Һ';
            case 30: return 'Ц';
            case 31: return 'Ч';
            case 32: return 'Ш';
            case 33: return 'Щ';
            case 34: return 'Ь';
            case 35: return 'Ы';
            case 36: return 'І';
            case 37: return 'Э';
            case 38: return 'Ю';
            case 39: return 'Я';
            case 40: return '-';
        }
        return ' ';
    }
    public static char getNumber(int i){
        switch (i){
            case 0: return '1';
            case 1: return '2';
            case 2: return '3';
            case 3: return '4';
            case 4: return '5';
            case 5: return '6';
            case 6: return '7';
            case 7: return '8';
            case 8: return '9';
            case 9: return '0';
        }
        return ' ';
    }
    public static String getDisseplinName(int i){
        switch (i){
            case 1: return "Математика";
            case 2: return "Физика";
            case 3: return "Биология";
            case 4: return "Химия";
        }
        return "";
    }
    public static char getABCDE(int i){
        switch (i){
            case 0: return 'A';
            case 1: return 'B';
            case 2: return 'C';
            case 3: return 'D';
            case 4: return 'E';
        }
        return ' ';
    }

}

import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class JOpenCV2  {
    public JOpenCV2(){
       // initComponents();
    }

    private void initComponents() {
        JFrame myWindow = new SimpleWindow();
        myWindow.setVisible(true);

    }

    public static void main(String[] args) throws IOException {
        Imaging imaging = new Imaging("myApp", 0);
        Result result = imaging.scan(Request.fromJson(
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
        PageScanner.rotate(res);


    }

    public void actionPerformed(ActionEvent e) {

    }
}

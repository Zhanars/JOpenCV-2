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
        initComponents();
    }

    private void initComponents() {
        JFrame myWindow = new SimpleWindow();
        myWindow.setVisible(true);

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JOpenCV2();
            }
        });


    }

    public void actionPerformed(ActionEvent e) {

    }
}

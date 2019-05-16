import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JOpenCV2 implements ActionListener {
    public JOpenCV2(){
        initComponents();
    }

    private void initComponents() {
        JFrame myWindow = new SimpleWindow();
        myWindow.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JOpenCV2();
            }
        });

    }

    public void actionPerformed(ActionEvent e) {

    }
}

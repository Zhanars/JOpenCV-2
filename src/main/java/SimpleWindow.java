import org.opencv.core.Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class SimpleWindow extends JFrame {
    private int varcount = 5;
    private int varsel = 0;
    private JButton button = new JButton("Начать");
    private JLabel col1 = new JLabel("Назавние ");
    private JLabel col2 = new JLabel("Значение ");
    private JLabel label1 = new JLabel("Номер потока:");
    private JLabel label2 = new JLabel("Сканировать:");
    private JLabel label3 = new JLabel("Кол-во бланков:");
    private JLabel label31 = new JLabel("dghf");
    private JLabel label4 = new JLabel("Кол-во ошибок:");
    private JLabel label41 = new JLabel("hgj");
    private JComboBox List1 = getList2(varcount,varsel);
    public SimpleWindow(){
        super("TestReader 1.5 - Сканирование бланков");
        this.setBounds(400,400,400,150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5,2));

        container.add(col1);
        container.add(col2);

        container.add(label1);
        container.add(List1);

        container.add(label2);
        button.addActionListener(new ButtonEventListener());
        container.add(button);

        container.add(label3);
        container.add(label31);

        container.add(label4);
        container.add(label41);
    }
    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String message = "";
                message += "Номер потока: " + (List1.getSelectedItem());
                Buffer.setNumberPotok((String) List1.getSelectedItem());
                Buffer.setColError(0);
                Buffer.setColBlank(0);
            JOptionPane.showMessageDialog(null,
                    message,
                    "Начинать сканирование?",
                    JOptionPane.WARNING_MESSAGE);
            //PageScanner.scan();
            try {
                PageScanner.rotate("images/123.jpg");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            label31.setText(String.valueOf(Buffer.getColBlank()));
            label41.setText(String.valueOf(Buffer.getColError()));
            JOptionPane.showMessageDialog(null,
                    "Бланки готовы",
                    "Отсканировано",
                    JOptionPane.PLAIN_MESSAGE);
        }

    }
    public static JComboBox getList2(int s, int sel){
        String[] items = new String[s];
        for (int i = 0; i < s; i++) {
            items[i] = Integer.toString(i + 1);
        }
        JComboBox  result = new JComboBox(items);
        result.setSelectedIndex(sel);
        result.setAlignmentX(LEFT_ALIGNMENT);
        return result;
    }
}

import org.opencv.core.Core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class SimpleWindow extends JFrame {
    private int incount = 25;
    private int insel = 24;
    private int varcount = 8;
    private int varsel = 7;
    private JButton button = new JButton("Создать бланк");
    private JLabel col1 = new JLabel(" Порядок ");
    private JLabel col2 = new JLabel(" Название предмета ");
    private JLabel col3 = new JLabel(" Кол-во вопросов ");
    private JLabel col4 = new JLabel(" Кол-во вариантов ");
    private JLabel col5 = new JLabel(" Включить ");
    private JLabel label1 = new JLabel("Предмет 1");
    private JLabel label2 = new JLabel("Предмет 2");
    private JLabel label3 = new JLabel("Предмет 3");
    private JLabel label4 = new JLabel("Предмет 4");
    private JLabel label5 = new JLabel("Предмет 5");
    private JComboBox CharList1 = getList();
    private JComboBox CharList2 = getList();
    private JComboBox CharList3 = getList();
    private JComboBox CharList4 = getList();
    private JComboBox CharList5 = getList();
    private JComboBox input1 = getList2(incount,insel);
    private JComboBox input2 = getList2(incount,insel);
    private JComboBox input3 = getList2(incount,insel);
    private JComboBox input4 = getList2(incount,insel);
    private JComboBox input5 = getList2(incount,insel);
    private JComboBox List1 = getList2(varcount,varsel);
    private JComboBox List2 = getList2(varcount,varsel);
    private JComboBox List3 = getList2(varcount,varsel);
    private JComboBox List4 = getList2(varcount,varsel);
    private JComboBox List5 = getList2(varcount,varsel);
    private JCheckBox check1 = new JCheckBox("Вкл", true);
    private JCheckBox check2 = new JCheckBox("Вкл", true);
    private JCheckBox check3 = new JCheckBox("Вкл", true);
    private JCheckBox check4 = new JCheckBox("Вкл", true);
    private JCheckBox check5 = new JCheckBox("Вкл", false);
    public SimpleWindow(){
        super("Конструктор бланка");
        this.setBounds(200,100,1000,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(7,5,2,2));

        container.add(col1);
        container.add(col2);
        container.add(col3);
        container.add(col4);
        container.add(col5);

        container.add(label1);
        container.add(CharList1);
        container.add(input1);
        container.add(List1);
        container.add(check1);

        container.add(label2);
        container.add(CharList2);
        container.add(input2);
        container.add(List2);
        container.add(check2);

        container.add(label3);
        container.add(CharList3);
        container.add(input3);
        container.add(List3);
        container.add(check3);

        container.add(label4);
        container.add(CharList4);
        container.add(input4);
        container.add(List4);
        container.add(check4);

        container.add(label5);
        container.add(CharList5);
        container.add(input5);
        container.add(List5);
        container.add(check5);

        button.addActionListener(new ButtonEventListener());
        container.add(button);

    }
    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String message = "";
            if (check1.isSelected()){
                message += "Первый предмет " + (CharList1.getSelectedItem());
                message += ", кол-во вопросов: " + (input1.getSelectedItem());
                message += ", кол-во вариантов: " + (List1.getSelectedItem());
            }
            if (check2.isSelected()){
                message += "\nВторой предмет " + (CharList2.getSelectedItem());
                message += ", кол-во вопросов: " + (input2.getSelectedItem());
                message += ", кол-во вариантов: " + (List2.getSelectedItem());
            }
            if (check3.isSelected()){
                message += "\nТретий предмет " + (CharList3.getSelectedItem());
                message += ", кол-во вопросов: " + (input3.getSelectedItem());
                message += ", кол-во вариантов: " + (List3.getSelectedItem());
            }
            if (check4.isSelected()){
                message += "\nЧетвертый предмет " + (CharList4.getSelectedItem());
                message += ", кол-во вопросов: " + (input4.getSelectedItem());
                message += ", кол-во вариантов: " + (List4.getSelectedItem());
            }
            if (check5.isSelected()){
                message += "\nПятый предмет " + (CharList5.getSelectedItem().toString());
                message += ", кол-во вопросов: " + (input5.getSelectedItem());
                message += ", кол-во вариантов: " + (List5.getSelectedItem());
            }
            JOptionPane.showMessageDialog(null,
                    message,
                    "Результат",
                    JOptionPane.WARNING_MESSAGE);
            //PageScanner.scan();
            try {
                PageScanner.rotate("2019-05-28_140448.jpg");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null,
                    "Бланк готов",
                    "Бланк готов",
                    JOptionPane.PLAIN_MESSAGE);
        }

    }
    public static JComboBox getList(){
        String[] items = {
                "Математика",
                "Казахский язык",
                "История Казахстана",
                "Физика",
                "Биология",
                "Химия",
                "География",
                "Всемирная история",
                "Английский язык",
                "Русский язык",
                "Казахская литература",
                "Русская литература",
                "Профильный предмет 1",
                "Профильный предмет 2"

        };
        JComboBox result = new JComboBox(items);
        result.setAlignmentX(LEFT_ALIGNMENT);
        return result;
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

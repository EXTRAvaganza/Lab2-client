import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;

public class GUI extends JFrame {
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel2;
    private JTabbedPane tabbedPane1;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton addButton;
    private JButton updateButton;
    private JButton delButton;
    private JButton delEmpButton;
    private JButton changeButton;
    private JButton changeButton1;
    private JButton searchEmpButton;
    private JButton searchDepButton;
    private client client;
    private String host;
    private int port;
    private boolean flagReader = true;

    class xmlParser {
        public void parser(String str) throws ParserConfigurationException, IOException, SAXException {
            Document document = convertStringToXMLDocument(str);
            assert document != null;
            Element element = document.getDocumentElement();
            switch (element.getAttribute("docType")) {
                case "message":
                    messageParser(document);
                    break;
                case "Employee":
                    employeesParser(document);
                    break;
                case "departments":
                    departmentsParser(document);
                    break;
            }
        }

        private Document convertStringToXMLDocument(String xmlString) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
                return doc;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void employeesParser(Document doc) {
            Element element = doc.getDocumentElement();
            NodeList nodeList = element.getChildNodes().item(0).getChildNodes();
            String[][] data = new String[nodeList.getLength()][7];
            for (int i = 0; i < nodeList.getLength(); i++) {
                data[i][0] = ((Element) nodeList.item(i)).getAttribute("firstName");
                data[i][1] = ((Element) nodeList.item(i)).getAttribute("middleName");
                data[i][2] = ((Element) nodeList.item(i)).getAttribute("lastName");
                data[i][3] = ((Element) nodeList.item(i)).getAttribute("salary");
                data[i][4] = ((Element) nodeList.item(i)).getAttribute("phone");
                data[i][5] = ((Element) nodeList.item(i)).getAttribute("department");
                data[i][6] = ((Element) nodeList.item(i)).getAttribute("id");
            }
            DefaultTableModel model = (DefaultTableModel) table2.getModel();
            clearTable(model);
            for (int i = 0; i < nodeList.getLength(); i++)
                model.addRow(new Object[]{data[i][6], data[i][2], data[i][0], data[i][1], data[i][3], data[i][4], data[i][5]});


        }

        private void departmentsParser(Document doc) {
            Element element = doc.getDocumentElement();
            NodeList nodeList = element.getChildNodes().item(0).getChildNodes();
            String[][] data = new String[nodeList.getLength()][3];
            for (int i = 0; i < nodeList.getLength(); i++) {
                data[i][0] = ((Element) nodeList.item(i)).getAttribute("name");
                data[i][1] = ((Element) nodeList.item(i)).getAttribute("director");
                data[i][2] = ((Element) nodeList.item(i)).getAttribute("id");
            }
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            clearTable(model);
            for (int i = 0; i < nodeList.getLength(); i++)
                model.addRow(new Object[]{data[i][2], data[i][0], data[i][1]});
        }

        private void messageParser(Document doc) {
            Element element = doc.getDocumentElement();
            NodeList list = element.getElementsByTagName("message");
            if (list.item(0) instanceof Element) {

                JOptionPane.showMessageDialog(new JOptionPane(), ((Element) list.item(0)).getAttribute("info"));
            }
        }
    }

    class reader extends Thread {
        @Override
        public void run() {
            while (flagReader) {
                try {
                    String temp = client.getReader().readLine();
                    if (temp != null)
                        (new xmlParser()).parser(temp);
                    else
                        break;
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Соединение потеряно. Перезапустите программу");
                    setFlagReader(false);
                }

            }
        }

    }

    private void setFlagReader(boolean flag) {
        flagReader = flag;
    }

    private void updateDeps() {
        send("-show -department");
    }

    private void updateEmps() {
        send("-show -employee");
    }

    private void shutdownWindow() {
        send("shutdown");
        client.close();
    }

    private void send(String info) {
        try {
            client.getWriter().write(info);
            client.getWriter().newLine();
            client.getWriter().flush();
        } catch (IOException ignored) {
        }
    }

    public GUI(String title) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super(title);
        this.setContentPane(panel1);
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setConnectInfo();
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                setFlagReader(false);
                shutdownWindow();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        this.pack();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDeps();
            }
        });
        client = new client(host, port);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                departmentInputDiaog t = new departmentInputDiaog();
                String temp[] = t.showDialog();
                try {
                    Integer.parseInt(temp[1]);
                    send("-create -department " + temp[0] + " " + temp[1]);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Проверьте правильность ввода данных(название без пробелов,ID директора - цифры)");
                }

            }
        });
        ((DefaultTableModel) table1.getModel()).addColumn("id");
        ((DefaultTableModel) table1.getModel()).addColumn("Имя");
        ((DefaultTableModel) table1.getModel()).addColumn("id - директора");
        ((DefaultTableModel) table2.getModel()).addColumn("ID - номер");
        ((DefaultTableModel) table2.getModel()).addColumn("Фамилия");
        ((DefaultTableModel) table2.getModel()).addColumn("Имя");
        ((DefaultTableModel) table2.getModel()).addColumn("Отчество");
        ((DefaultTableModel) table2.getModel()).addColumn("Зарплата");
        ((DefaultTableModel) table2.getModel()).addColumn("Номер телефона");
        ((DefaultTableModel) table2.getModel()).addColumn("Отдел");


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeeInputDialog t = new employeeInputDialog();
                String temp[] = t.showDialog();
                send("-create -employee " + temp[1] + " " + temp[0] + " " + temp[2] + " " + temp[5] + " " + temp[3] + " " + temp[4]);
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send("-show -employee");
            }
        });
        new reader().start();
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    send("-delete -department " + Integer.parseInt(JOptionPane.showInputDialog("Введите id удаляемого отдела:")));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Вы ввели неверные данные");
                }
                updateDeps();
            }
        });
        delEmpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    send("-delete -employee " + Integer.parseInt(JOptionPane.showInputDialog("Введите id удаляемого сотрудника:")));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Вы ввели неверные данные");
                }
                updateEmps();
            }
        });
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeEmpDialog changeDialog = new changeEmpDialog();
                String ID = changeDialog.getID();
                String newValue = changeDialog.getNewValue();
                String attr = changeDialog.getAttr();
                send("-change -employee " + ID + " " + attr + " " + newValue);
                updateEmps();
            }
        });
        changeButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDepDialog dialog = new changeDepDialog();
                String ID = dialog.getID();

                String newValue = dialog.getNewValue();
                String attr = dialog.getAttr();
                if (ID.equals(""))
                    JOptionPane.showMessageDialog(new JOptionPane(), "Не указан ID изменяемого объекта");
                else if (newValue.equals(""))
                    JOptionPane.showMessageDialog(new JOptionPane(), "Не указано новое значение");
                else {
                    send("-change -department " + ID + " " + attr + " " + newValue);
                }
            }
        });
        searchEmpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchEmpDialog t = new searchEmpDialog();
                send("-search -employee " + t.getAttr() + " " + t.getValue());
            }
        });
        searchDepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDepDialog t = new searchDepDialog();
                send("-search -department " + t.getAttr() + " " + t.getValue());
            }
        });

    }

    private void setConnectInfo() {
        boolean flag = true;
        String one = "Введите хост (Default: \"localhost\")";
        String two = "Введите порт";
        while (flag) {
            try {
                flag = false;
                host = JOptionPane.showInputDialog(one);
                port = Integer.parseInt(JOptionPane.showInputDialog(two));
            } catch (Exception e) {
                one = "Проверьте правильность хоста и повторите ввод (Default: \"localhost\")";
                two = "Проверьте правильность порта и повторите ввод";
                flag = true;
            }
        }
    }

    private void clearTable(DefaultTableModel table) {
        for (int i = table.getRowCount() - 1; i >= 0; i--) {
            table.removeRow(i);
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-14082062));
        panel1.setEnabled(true);
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 10, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Кадры", panel3);
        scrollPane2 = new JScrollPane();
        panel3.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 10, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table2 = new JTable();
        scrollPane2.setViewportView(table2);
        delEmpButton = new JButton();
        delEmpButton.setText("Удалить");
        panel3.add(delEmpButton, new com.intellij.uiDesigner.core.GridConstraints(1, 9, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addButton = new JButton();
        addButton.setText("Добавить");
        panel3.add(addButton, new com.intellij.uiDesigner.core.GridConstraints(1, 8, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Обновить");
        panel3.add(updateButton, new com.intellij.uiDesigner.core.GridConstraints(1, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        changeButton = new JButton();
        changeButton.setText("Изменить");
        panel3.add(changeButton, new com.intellij.uiDesigner.core.GridConstraints(1, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchEmpButton = new JButton();
        searchEmpButton.setText("Поиск");
        panel3.add(searchEmpButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 6, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Отделы", panel2);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null));
        scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        delButton = new JButton();
        delButton.setText("Удалить");
        panel2.add(delButton, new com.intellij.uiDesigner.core.GridConstraints(1, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button2 = new JButton();
        button2.setText("Добавить");
        panel2.add(button2, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button1 = new JButton();
        button1.setText("Обновить");
        panel2.add(button1, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        changeButton1 = new JButton();
        changeButton1.setText("Изменить");
        panel2.add(changeButton1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchDepButton = new JButton();
        searchDepButton.setText("Поиск");
        panel2.add(searchDepButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}

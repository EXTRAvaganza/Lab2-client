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
import java.io.IOException;
import java.io.StringReader;

public class GUI extends JFrame {

    class xmlParser {
        public void parser(String str) throws ParserConfigurationException, IOException, SAXException {
            Document document = convertStringToXMLDocument(str);
            Element element = document.getDocumentElement();
            if (element.getAttribute("docType").equals("message"))
                messageParser(document);
            else if (element.getAttribute("docType").equals("Employee"))
                employeesParser(document);
            else if (element.getAttribute("docType").equals("departments"))
                departmentsParser(document);
            System.out.println(str);
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
            switch (doc.getDocumentElement().getAttribute("eventType")) {
                case "list": {

                    Element element = doc.getDocumentElement();
                    NodeList nodeList = element.getChildNodes().item(0).getChildNodes();
                    String[][] data = new String[nodeList.getLength()][6];
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        data[i][0] = ((Element) nodeList.item(i)).getAttribute("firstName");
                        data[i][1] = ((Element) nodeList.item(i)).getAttribute("middleName");
                        data[i][2] = ((Element) nodeList.item(i)).getAttribute("lastName");
                        data[i][3] = ((Element) nodeList.item(i)).getAttribute("salary");
                        data[i][4] = ((Element) nodeList.item(i)).getAttribute("phone");
                        data[i][5] = ((Element) nodeList.item(i)).getAttribute("department");
                    }
                    DefaultTableModel model = (DefaultTableModel) table2.getModel();
                    clearTable(model);
                    for (int i = 0; i < nodeList.getLength(); i++)
                        model.addRow(new Object[]{data[i][0], data[i][1], data[i][2], data[i][3], data[i][4], data[i][5]});
                    break;
                }
                case "add": {
                    break;
                }
                case "del": {
                    break;
                }
                case "change": {
                    break;
                }
            }
        }

        private void departmentsParser(Document doc) {
            Element element = doc.getDocumentElement();
            NodeList nodeList = element.getChildNodes().item(0).getChildNodes();
            String[][] data = new String[nodeList.getLength()][2];
            for (int i = 0; i < nodeList.getLength(); i++) {
                data[i][0] = ((Element) nodeList.item(i)).getAttribute("name");
                data[i][1] = ((Element) nodeList.item(i)).getAttribute("director");
                System.out.println(((Element) nodeList.item(i)).getAttribute("name"));
            }
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            clearTable(model);
            System.out.println(data[0][0] + data[0][1]);
            for (int i = 0; i < nodeList.getLength(); i++)
                model.addRow(new Object[]{data[i][0], data[i][1]});
        }

        private void messageParser(Document doc) {
            Element element = doc.getDocumentElement();
            NodeList list = element.getElementsByTagName("message");
            if (list.item(0) instanceof Element)
                System.out.println(((Element) list.item(0)).getAttribute("info"));
        }
    }

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
    private Client client;

    class reader extends Thread {
        @Override
        public void run() {
            while (true) {
                try {

                    (new xmlParser()).parser(client.getReader().readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public GUI(String title) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super(title);
        client = new Client();
        this.setContentPane(panel1);
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.getWriter().write("-show -department");
                    client.getWriter().newLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    client.getWriter().flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                String temp = "";
                try {
                    temp = client.getReader().readLine();
                    System.out.println(temp);
                    new xmlParser().parser(temp);
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (SAXException ex) {
                    ex.printStackTrace();
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                }
                ;
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                departmentInputDiaog t = new departmentInputDiaog();
                String temp[] = t.showDialog();
                try {
                    client.getWriter().write("-create -department " + temp[0] + " " + temp[1]);
                    client.getWriter().newLine();
                    client.getWriter().flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        ((DefaultTableModel) table1.getModel()).addColumn("name");
        ((DefaultTableModel) table1.getModel()).addColumn("director");
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
                try {
                    client.getWriter().write("-create -employee " + temp[1] + " " + temp[0] + " " + temp[2] + " " + temp[3] + " " + temp[4] + " " + temp[5]);
                    client.getWriter().newLine();
                    client.getWriter().flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.getWriter().write("-show -employee");
                    client.getWriter().newLine();
                    client.getWriter().flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        new reader().start();
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
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Кадры", panel3);
        scrollPane2 = new JScrollPane();
        panel3.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table2 = new JTable();
        scrollPane2.setViewportView(table2);
        addButton = new JButton();
        addButton.setText("Добавить");
        panel3.add(addButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Обновить");
        panel3.add(updateButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Отделы", panel2);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null));
        scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        button2 = new JButton();
        button2.setText("Добавить");
        panel2.add(button2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button1 = new JButton();
        button1.setText("Обновить");
        panel2.add(button1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}

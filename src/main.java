import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        GUI gui =new GUI("My App");
        gui.setVisible(true);
       // new JOptionPaneTest();
    }
}
class JOptionPaneTest extends JFrame
{
    private static final long serialVersionUID = 1L;

    private        JPanel  contents       = null;

    private        JButton btnInput1      = null;
    private        JButton btnInput2      = null;
    private        JButton btnInput3      = null;

    private      ImageIcon  icon          = null;
    private        String[] drink         = {"Сок",
            "Минералка",
            "Лимонад"  ,
            "Пиво"};
    public JOptionPaneTest()
    {
        super("Пример использования JOptionPane");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Локализация кнопок
        UIManager.put("OptionPane.yesButtonText"   , "Да"    );
        UIManager.put("OptionPane.noButtonText"    , "Нет"   );
        UIManager.put("OptionPane.cancelButtonText", "Отмена");

        contents = new JPanel();
        // Иконка для отображения в окне сообщений
        btnInput1 = new JButton("InputDialog 2+3");
        btnInput2 = new JButton("InputDialog 4");
        btnInput3 = new JButton("InputDialog 7");
        addInputListeners  ();

        // Размещение кнопок в интерфейсе
        contents.add(btnInput1);
        contents.add(btnInput2);
        contents.add(btnInput3);

        setContentPane(contents);
        // Вывод окна на экран
        this.pack();
        setVisible(true);
    }
    private void addInputListeners()
    {
        btnInput1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Диалоговое окно ввода данных : родитель, HTML сообщение
                String result = JOptionPane.showInputDialog(
                        JOptionPaneTest.this,
                        "<html><h2>Добро пожаловать");
                JOptionPane.showInputDialog(
                        JOptionPaneTest.this,
                        "Вы ответили", result);
            }
        });
        btnInput2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Диалоговое окно ввода данных : родитель, сообщение в виде
                // массива строк, тип диалогового окна (иконки)
                JOptionPane.showInputDialog(JOptionPaneTest.this,
                        new String[] {"Неверно введен пароль!",
                                "Повторите пароль :"},
                        "Авторизация",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        btnInput3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Диалоговое окно ввода данных
                Object result = JOptionPane.showInputDialog(
                        JOptionPaneTest.this,
                        "Выберите любимый напиток :",
                        "Выбор напитка",
                        JOptionPane.QUESTION_MESSAGE,
                        icon, drink, drink[0]);
                // Диалоговое окно вывода сообщения
                JOptionPane.showMessageDialog(JOptionPaneTest.this, result);
            }
        });
    }
}
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements controller {
    private String host;
    private int port;
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    Client() throws IOException {
        connect();
    }
    public BufferedReader getReader()
    {
        return in;
    }
    public BufferedWriter getWriter()
    {
        return out;
    }

    @Override
    public void connect() {
        boolean flag = true;
        String one = "Введите хост";
        String two = "Введите порт";
        while(flag) {
            try {
                flag = false;
                host = JOptionPane.showInputDialog(one);
                port = Integer.parseInt(JOptionPane.showInputDialog(two));
                clientSocket = new Socket("localhost", 4400);
            } catch (Exception e) {
                one = "Проверьте правильность хоста и повторите ввод";
                two = "Проверьте правильность порта и повторите ввод";
                flag = true;
            }
        }
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }
        catch (IOException e){e.printStackTrace();}
    }
}

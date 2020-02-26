import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class client implements controller {


    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private String host = "localhost";
    private int port = 4400;
    client(String host,int port) throws IOException {
        this.host = host;
        this.port = port;
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
        try {
            clientSocket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }
        catch (IOException e){e.printStackTrace();}
    }
    public void close() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        }
        catch (IOException ignored){}
    }
}

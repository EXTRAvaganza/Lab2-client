import java.io.*;
import java.net.Socket;

public class client implements controller {
    class reader extends Thread {
        @Override
        public void run() {
            while(true)
            {
                try {
                    System.out.println(in.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class writer extends Thread {
        @Override
        public void run() {

            while(true)
            {
                try {
                    String word = reader.readLine();
                    out.write(word + "\n");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;
    public client() throws IOException {
        connect();
    }
    @Override
    public void connect() throws IOException {
        try {
            try {
                clientSocket = new Socket("localhost", 4400);
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                new reader().start();
                new writer().start();
            } finally { // в любом случае необходимо закрыть сокет и потоки
                /*System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
*/            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
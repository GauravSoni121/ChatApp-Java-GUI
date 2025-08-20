import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    // Constructor
    public Server() {
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server is ready to accept connection...");
            socket = serverSocket.accept();
            System.out.println("Connected with client.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            // yaha startWriting() ko call karna hai
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reading thread
    public void startReading() {
        Runnable r1 = () -> {
            try {
                String msg;
                while ((msg = br.readLine()) != null) {
                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }

    // 👇 Ye wala method tumhe likhna hai
    public void startWriting() {
        Runnable r2 = () -> {
            try {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String content = consoleReader.readLine();
                    out.println(content);
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        new Server();
    }
}

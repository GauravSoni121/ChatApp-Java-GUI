import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatGUI extends JFrame {

    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;

    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    public ChatGUI() {
        setTitle("Chat Application");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        textField = new JTextField();
        sendButton = new JButton("Send");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // connect to server
        try {
            socket = new Socket("127.0.0.1", 1234);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            textArea.append("Connected to server...\n");

            // start reading thread
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = br.readLine()) != null) {
                        textArea.append("Server: " + msg + "\n");
                    }
                } catch (Exception e) {
                    textArea.append("Connection closed.\n");
                }
            }).start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server!");
            e.printStackTrace();
        }

        // send message on button click
        sendButton.addActionListener(e -> sendMessage());
        textField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String msg = textField.getText();
        if (!msg.isEmpty()) {
            textArea.append("Me: " + msg + "\n");
            out.println(msg);
            textField.setText("");
            if (msg.equalsIgnoreCase("exit")) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatGUI().setVisible(true);
        });
    }
}

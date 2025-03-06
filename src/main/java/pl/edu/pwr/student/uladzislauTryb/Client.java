package pl.edu.pwr.student.uladzislauTryb;

import java.io.*;
import java.net.Socket;

public class Client {

    private int port;
    private final String host;

    private static BufferedReader in;
    private static BufferedWriter out;

    public Client(int port, String host) throws IOException {
        this.port = port;
        this.host = host;
        //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMessage(String message) throws IOException {
        try {
            Socket clientSocket = new Socket(host, port);
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            System.err.println(e);
        }

    }
    public String getMessage() throws IOException {
        try {
            Socket clientSocket = new Socket("localhost", port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return in.readLine();
        } catch (IOException e) {}
        return "";
    }
    public String sendMessageWithAnswear(String message) throws IOException {
        try {
            Socket clientSocket = new Socket("localhost", port);

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            out.write(message + "\n");
            out.flush();

            return in.readLine();
        } catch (IOException e) {
            System.err.println(e);
        }
        return "";
    }
}
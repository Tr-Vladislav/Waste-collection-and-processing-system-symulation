package pl.edu.pwr.student.uladzislauTryb;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ISewagePlant extends Thread{

    private Socket clientSocket;
    private ServerSocket server;
    private BufferedReader in;
    private BufferedWriter out;
    private SewagePlant sewagePlant;

    public ISewagePlant(int port, SewagePlant sewagePlant) throws IOException {
        this.sewagePlant = sewagePlant;
        server = new ServerSocket(port);
        start();
    }

    @Override
    public void run() {
        try {
            try {
                while(true) {
                    clientSocket = server.accept();
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    String request = in.readLine();
                    System.out.println(request);
                    if (request.startsWith("spi:")) {
                        String[] parts = request.split(":")[1].split(",");
                        int number = Integer.parseInt(parts[0]);
                        int volume = Integer.parseInt(parts[1]);
                        sewagePlant.setPumpIn(number, volume);
                    } else if (request.startsWith("gs:")) {
                        int number = Integer.parseInt(request.split(":")[1]);
                        int sum = sewagePlant.getStatus(number);
                        out.write(sum + "\n");
                        out.flush();
                    } else if (request.startsWith("spo:")) {
                        int number = Integer.parseInt(request.split(":")[1]);
                        sewagePlant.setPayoff(number);
                    }
                    Thread.sleep(100);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                clientSocket.close();

                in.close();
                out.close();
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

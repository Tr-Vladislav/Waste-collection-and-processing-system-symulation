package pl.edu.pwr.student.uladzislauTryb;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ITanker extends Thread {

    private Socket clientSocket;
    private ServerSocket server;
    private BufferedReader in;
    private BufferedWriter out;
    private Tanker tanker;

    public ITanker(int port,Tanker tanker) throws IOException {
        this.tanker = tanker;
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
                    if (request.startsWith("sj:")) {
                        String[] parts = request.split(":")[1].split(",");
                        int port = Integer.parseInt(parts[0]);
                        String host = parts[1];
                        tanker.setJob(port, host);
                        System.out.println("Get Job");
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

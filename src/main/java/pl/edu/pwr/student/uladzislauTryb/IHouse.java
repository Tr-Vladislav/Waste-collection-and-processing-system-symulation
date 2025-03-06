package pl.edu.pwr.student.uladzislauTryb;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class IHouse extends Thread {

    private Socket clientSocket;
    private ServerSocket server;
    private BufferedReader in;
    private BufferedWriter out;
    private House house;



    public IHouse(int port,House house) throws IOException {
        this.house = house;
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
                    if (request.startsWith("gp:")) {
                        int max = Integer.parseInt(request.split(":")[1]);
                        int pumpedOut = house.getPumpOut(max);
                        System.out.println(pumpedOut);
                        out.write(pumpedOut + "\n");
                        out.flush();
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

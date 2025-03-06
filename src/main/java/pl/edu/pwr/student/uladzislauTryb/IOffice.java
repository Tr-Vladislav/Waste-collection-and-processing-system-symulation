package pl.edu.pwr.student.uladzislauTryb;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class IOffice extends Thread {

    private Socket clientSocket;
    private ServerSocket server;
    private BufferedReader in;
    private BufferedWriter out;
    private Office office;



    public IOffice(int port, Office office) throws IOException {
        this.office = office;
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
                    if (request.startsWith("o:")) {
                        String[] parts = request.split(":")[1].split(",");
                        int port = Integer.parseInt(parts[0]);
                        String host = parts[1];

                        int isPumpedOut = office.order(port, host);
                        System.out.println(isPumpedOut);
                        out.write(isPumpedOut + "\n");
                        out.flush();
                    }else if(request.startsWith("r:")){
                        String[] parts = request.split(":")[1].split(",");
                        int port = Integer.parseInt(parts[0]);
                        String host = parts[1];

                        int num = office.register(port, host);
                        out.write(num + "\n");
                        out.flush();
                    }else if(request.startsWith("sr:")){
                        int num = Integer.parseInt(request.split(":")[1]);
                        office.setReadyToServe(num);
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

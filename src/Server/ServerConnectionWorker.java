package Server;

import java.io.*;
import java.net.Socket;

public class ServerConnectionWorker implements Runnable{
    protected Socket clientSocket = null;
    public ServerConnectionWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            System.out.println("Rozpoczęto obsługę połączenia");
            PrintWriter  out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader  in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.close();
            in.close();
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}

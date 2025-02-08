package Server;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ServerConnectionWorker implements Runnable{
    protected Socket clientSocket = null;
    private boolean isAuthorized = false;
    public ServerConnectionWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private void HandleLoginCommand(JSONObject command) {

    }

    public void run() {
        try {
            System.out.println("Rozpoczęto obsługę połączenia");
            PrintWriter  out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader  in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JSONObject command = new JSONObject(inputLine);
                int commandType = command.getInt("Command");
                switch (commandType){
                    case 0:
                        System.out.println("Otrzymano komende logowania");
                        HandleLoginCommand(command);
                        break;
                    case 1:
                        System.out.println("Otrzymano komende utworzenia użytkownika");
                        break;
                    case 2:
                        System.out.println("Otrzymano komende dodania książki");
                        break;
                    default:
                        System.out.println("Nieznany typ komendy: " + commandType);
                        break;
                }
            }
            out.close();
            in.close();
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}

package Server;

import Common.WebSocketResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import static Server.DatabaseConfig.*;
import static Server.ServerMain.*;
import static Server.WebsocketHandlers.LoginHandler.HandleLoginCommand;

public class ServerConnectionWorker implements Runnable{
    protected Socket clientSocket = null;
    private boolean isAuthorized = false;
    private int userRole = -1;
    public ServerConnectionWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void sendUnauthorizedResponse(PrintWriter  out){
        WebSocketResponse response = new WebSocketResponse(-1, "unauthorized");
        JSONObject responseJson = new JSONObject(response, new String[]{"Status", "Message"});
        out.println(responseJson.toString());
    }

    public void run() {
        try {
            System.out.println("Rozpoczęto obsługę połączenia");
            PrintWriter  out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader  in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JSONObject command = new JSONObject(inputLine);
                System.out.println(inputLine);
                int commandType = command.getInt("Command");
                switch (commandType){
                    case 0:
                        System.out.println("Otrzymano komende logowania");
                        int authResult = HandleLoginCommand(command, out);
                        if(authResult >= 0)
                        {
                            isAuthorized = true;
                            userRole = authResult;
                        }
                        break;
                    case 1:
                        System.out.println("Otrzymano komende utworzenia użytkownika");
                        if(isAuthorized && (userRole == 0 || userRole == 1)) {
                            //handle
                        }
                        else {
                            sendUnauthorizedResponse(out);
                        }
                        break;
                    case 2:
                        System.out.println("Otrzymano komende dodania książki");
                        if(isAuthorized && (userRole == 0 || userRole == 1)) {
                            //handle
                        }
                        else {
                            sendUnauthorizedResponse(out);
                        }
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

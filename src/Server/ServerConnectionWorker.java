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

public class ServerConnectionWorker implements Runnable{
    protected Socket clientSocket = null;
    private boolean isAuthorized = false;
    private int userRole = -1;
    public ServerConnectionWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private void HandleLoginCommand(JSONObject command, PrintWriter  out) {
        try{
            Connection connection = connectToDatabase("jdbc:mysql://", DataBaseAddress + ":" + DataBasePort, "", "root", "root");
            Statement st = createStatement(connection);
            executeUpdate(st, "USE " + DataBaseName + ";");
            JSONArray arguments = command.getJSONArray("Arguments");
            ResultSet resultSet = executeQuery(st, UsersDataModel.GetSelectStatementByUserName(arguments.getString(0)));
            String password = resultSet.getString(UsersDataModel.Password);
            if(Objects.equals(password, arguments.getString(1))){
                isAuthorized = true;
                userRole = resultSet.getInt(UsersDataModel.UserRole);
                WebSocketResponse response = new WebSocketResponse(0, "login success");
                JSONObject responseJson = new JSONObject(response);
                out.println(responseJson.toString());
            }
            else {
                WebSocketResponse response = new WebSocketResponse(1, "login failure");
                JSONObject responseJson = new JSONObject(response);
                out.println(responseJson.toString());
            }
        }catch (Exception e)
        {
            WebSocketResponse response = new WebSocketResponse(1, "login failure");
            JSONObject responseJson = new JSONObject(response);
            out.println(responseJson.toString());
            System.out.println("Błąd przy obsłudze logowania");
            e.printStackTrace();
        }
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
                        HandleLoginCommand(command, out);
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

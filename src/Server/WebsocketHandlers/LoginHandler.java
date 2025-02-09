package Server.WebsocketHandlers;

import Common.WebSocketResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import static Server.DatabaseConfig.*;
import static Server.ServerMain.*;
import static Server.ServerMain.executeQuery;

public class LoginHandler {
    protected static void sendLoginFailedResponse(PrintWriter out)
    {
        WebSocketResponse response = new WebSocketResponse(-1, "login failure");
        JSONObject responseJson = new JSONObject(response, new String[]{"Status", "Message"});
        out.println(responseJson.toString());
    }

    public static int HandleLoginCommand(JSONObject command, PrintWriter out) {
        try{
            Connection connection = connectToDatabase("jdbc:mysql://", DataBaseAddress + ":" + DataBasePort, "", "root", "root");
            Statement st = createStatement(connection);
            executeUpdate(st, "USE " + DataBaseName + ";");
            JSONArray arguments = command.getJSONArray("Arguments");
            String sqlString = UsersDataModel.GetSelectStatementByUserName(arguments.getString(0));
            System.out.println(sqlString);
            ResultSet resultSet = executeQuery(st, sqlString);
            if(resultSet == null) {
                sendLoginFailedResponse(out);
            }
            else{
                if(resultSet.next()) {
                    String password = resultSet.getString(UsersDataModel.Password);
                    if (Objects.equals(password, arguments.getString(1))) {
                        int userRole = resultSet.getInt(UsersDataModel.UserRole);
                        WebSocketResponse response = new WebSocketResponse(userRole, "login success");
                        JSONObject responseJson = new JSONObject(response, new String[]{"Status", "Message"});
                        out.println(responseJson.toString());
                        return userRole;
                    }
                    else {
                        sendLoginFailedResponse(out);
                    }
                } else {
                    sendLoginFailedResponse(out);
                }
                resultSet.close();
                st.close();
            }
        }catch (Exception e)
        {
            sendLoginFailedResponse(out);
            System.out.println("Błąd przy obsłudze logowania");
            e.printStackTrace();
        }
        return -1;
    }
}

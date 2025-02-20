package Server.WebsocketHandlers;

import Common.UserDto;
import Common.WebSocketResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static Server.DatabaseConfig.*;
import static Server.ServerMain.*;

public class AddNewUserHandler {

    protected static void sendActionFailedResponse(PrintWriter out)
    {
        WebSocketResponse response = new WebSocketResponse(-1, "User Action Failed");
        JSONObject responseJson = new JSONObject(response, new String[]{"Status", "Message"});
        out.println(responseJson.toString());
    }
    public static void AddNewUser(JSONObject command, PrintWriter out)
    {
        try{
            Connection connection = connectToDatabase("jdbc:mysql://", DataBaseAddress + ":" + DataBasePort, "", "root", "root");
            Statement st = createStatement(connection);
            executeUpdate(st, "USE " + DataBaseName + ";");
            JSONArray arguments = command.getJSONArray("Arguments");
            if(arguments.length()==4) {
                String sqlString = UsersDataModel.GetInsertStatementString(arguments.getString(0), Integer.parseInt(arguments.getString(1)), arguments.getString(2), arguments.getString(3));
                System.out.println(sqlString);
                if(executeUpdate(st, sqlString)==1)
                {
                    WebSocketResponse response = new WebSocketResponse(0, "Adding User Success");
                    JSONObject responseJson = new JSONObject(response, new String[]{"Status", "Message"});
                    out.println(responseJson.toString());
                }
            }
            st.close();
            connection.close();
        }catch (Exception e)
        {
            System.out.println("Błąd przy obsłudze dodawania użytkownika");
            e.printStackTrace();
        }
        sendActionFailedResponse(out);
    }

    public static void GetUserData(JSONObject command, PrintWriter out){
        try{
            Connection connection = connectToDatabase("jdbc:mysql://", DataBaseAddress + ":" + DataBasePort, "", "root", "root");
            Statement st = createStatement(connection);
            executeUpdate(st, "USE " + DataBaseName + ";");
            JSONArray arguments = command.getJSONArray("Arguments");
            if(arguments.length()==1) {
                String sqlString = UsersDataModel.GetSelectStatementByUserName(arguments.getString(0));
                System.out.println(sqlString);
                ResultSet result = executeQuery(st, sqlString);
                if(result != null && result.next())
                {
                    UserDto userResult = new UserDto();
                    userResult.UserName = result.getString(UsersDataModel.UserName);
                    userResult.UserRole = result.getInt(UsersDataModel.UserRole);
                    userResult.Mail = result.getString(UsersDataModel.Mail);
                    JSONObject userJson = new JSONObject(userResult, new String[]{"UserName","Mail","UserRole"});
                    WebSocketResponse response = new WebSocketResponse(0, userJson.toString());
                    JSONObject responseJson = new JSONObject(response, new String[]{"Status", "Message"});
                    out.println(responseJson.toString());
                }
            }
            st.close();
            connection.close();
        }catch (Exception e)
        {
            System.out.println("Błąd przy obsłudze dodawania użytkownika");
            e.printStackTrace();
        }
        sendActionFailedResponse(out);
    }
}

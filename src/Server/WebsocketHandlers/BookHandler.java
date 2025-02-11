package Server.WebsocketHandlers;

import Common.BookDto;
import Common.WebSocketResponse;
import Server.DatabaseConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static Server.DatabaseConfig.*;
import static Server.ServerMain.*;
import static Server.ServerMain.executeUpdate;

public class BookHandler {
    protected static void sendActionFailedResponse(PrintWriter out)
    {
        WebSocketResponse response = new WebSocketResponse(-1, "action failure");
        JSONObject responseJson = new JSONObject(response, new String[]{"Status", "Message"});
        out.println(responseJson.toString());
    }
    public static void AddBook(JSONObject command, PrintWriter out) {
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

    public static void GetAllBooks(PrintWriter out){
        try{
            Connection connection = connectToDatabase("jdbc:mysql://", DataBaseAddress + ":" + DataBasePort, "", "root", "root");
            Statement st = createStatement(connection);
            executeUpdate(st, "USE " + DataBaseName + ";");
            String sqlString = UsersDataModel.GetSelectStatementAll();
            System.out.println(sqlString);
            ResultSet result = executeQuery(st, sqlString);
            ArrayList<BookDto> bookList = new ArrayList<>();
            if(result == null)
            {
                sendActionFailedResponse(out);
                return;
            }
            while ((result.next())){
                bookList.add(new BookDto(result.getInt(RentablesDataModel.Id), result.getString(RentablesDataModel.IBAN), result.getString(RentablesDataModel.Title), result.getString(RentablesDataModel.Author)));
            }
            JSONArray bookListJson = new JSONArray(bookList);
            WebSocketResponse response = new WebSocketResponse(0, bookListJson.toString());
            JSONObject responseJson = new JSONObject(response, new String[]{"Status", "Message"});
            out.println(responseJson.toString());
            System.out.println(responseJson.toString());
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

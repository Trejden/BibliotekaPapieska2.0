package Server;
import java.sql.*;
import static Server.DatabaseConfig.*;

public class ServerMain {


    public static Connection connectToDatabase(String kindOfDatabase, String adress,
                                               String dataBaseName, String userName, String password) {
        System.out.println("\nŁączenie z bazą danych:");
        String baza = kindOfDatabase + adress + "/" + dataBaseName;
        java.sql.Connection connection = null;
        try {
            connection = DriverManager.getConnection(baza, userName, password);
        } catch (SQLException e) {
            System.out.println("Błąd przy połączeniu z bazą danych!");
            e.printStackTrace();
            System.exit(1);
        }
        return connection;
    }

    public static Statement createStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Błąd createStatement! " +
                    e.getMessage() + ": " + e.getErrorCode());
            System.exit(3);
        }
        return null;
    }

    public static void closeConnection(Connection connection, Statement s) {
        System.out.println("\nZamykanie połączenia z bazą:");
        try {
            s.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Błąd przy zamykaniu połączenia z bazą! " +
                                e.getMessage() + ": " + e.getErrorCode());
            System.exit(4);
        }
        System.out.println("zamknięcie OK");
    }

    public static ResultSet executeQuery(Statement s, String sql) {
        try {
            return s.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Zapytanie nie wykonane! " +
                                e.getMessage() + ": " + e.getErrorCode());
        }
        return null;
    }

    public static int executeUpdate(Statement s, String sql) {
        try {
            return s.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Zapytanie nie wykonane! " +
                                e.getMessage() + ": " + e.getErrorCode());
        }
        return -1;
    }

    private static void EnsureDataBaseAndTablesExists() {
        //upewniamy się, że bazadanych istnieje
        try {
            Connection connection = connectToDatabase("jdbc:mysql://", DataBaseAddress + ":" + DataBasePort, "", "root", "root");
            Statement st = createStatement(connection);
            if (executeUpdate(st, "USE " + DataBaseName + ";") == 0)
                System.out.println("Udane wybranie bazy danych");
            else {
                System.out.println("Baza nie istnieje! Tworzymy bazę danych: ");
                if (executeUpdate(st, "create Database " + DataBaseName + ";") == 1)
                    System.out.println("Baza utworzona");
                else
                    System.out.println("Nie udało się utworzyć bazy danych!");
                if (executeUpdate(st, "USE " + DataBaseName + ";") == 0)
                    System.out.println("Udane wybranie bazy danych");
                else {
                    System.out.println("Baza nie istnieje!");
                    System.exit(4);
                }
            }
            st.close();

            Statement st2 = createStatement(connection);
            System.out.println("Sprawdzam czy tablica " + UsersDataModel.TableName + " istnieje");
            if(executeQuery(st2, "Select * from " + UsersDataModel.TableName + " LIMIT 1") == null)
            {
                System.out.println("nie ma tablicy, tworzę ją");
                Statement st3 = createStatement(connection);
                executeUpdate(st3, UsersDataModel.GetSqlCreationString());
                st3.close();
                System.out.println("Dodaje domyslnego administratora systemu, pamiętaj by zmienić mu hasło!");
                Statement adminCreate = createStatement(connection);
                System.out.println(UsersDataModel.GetInsertStatementString("Admin", 1, "Admin", "NULL"));
                executeUpdate(adminCreate, UsersDataModel.GetInsertStatementString("Admin", 1, "Admin", "NULL"));
                adminCreate.close();
            }
            System.out.println("Sprawdzam czy tablica " + RentablesDataModel.TableName + " istnieje");
            if(executeQuery(st2, "Select * from " + RentablesDataModel.TableName + " LIMIT 1") == null)
            {
                System.out.println("nie ma tablicy, tworzymy ją");
                Statement st4 = createStatement(connection);
                executeUpdate(st4, RentablesDataModel.GetSqlCreationString());
                st4.close();
            }
            System.out.println("Sprawdzam czy tablica " + ReservationsDataModel.TableName + " istnieje");
            if(executeQuery(st2, "Select * from " + ReservationsDataModel.TableName + " LIMIT 1") == null)
            {
                System.out.println("nie ma tablicy, tworzymy ją");
                Statement st5 = createStatement(connection);
                executeUpdate(st5, ReservationsDataModel.GetSqlCreationString());
                st5.close();
            }
            System.out.println("Sprawdzam czy tablica " + RentalsDataModel.TableName + " istnieje");
            if(executeQuery(st2, "Select * from " + RentalsDataModel.TableName + " LIMIT 1") == null)
            {
                System.out.println("nie ma tablicy, tworzymy ją");
                Statement st6 = createStatement(connection);
                executeUpdate(st6, RentalsDataModel.GetSqlCreationString());
                st6.close();
            }
            System.out.println("Sprawdzam czy tablica " + PenaltiesDataModel.TableName + " istnieje");
            if(executeQuery(st2, "Select * from " + PenaltiesDataModel.TableName + " LIMIT 1") == null)
            {
                System.out.println("nie ma tablicy, tworzymy ją");
                Statement st7 = createStatement(connection);
                executeUpdate(st7, PenaltiesDataModel.GetSqlCreationString());
                st7.close();
            }

            st2.close();
            connection.close();
        }catch (Exception e)
        {
            System.out.println("Wystąpił błąd, przy ustalaniu bazy danych");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Witamy w aplikacji serwerowej biblioteki papieskiej w wersji 2.0");
        try{
            EnsureDataBaseAndTablesExists();
            LibraryServer server = new LibraryServer(9000);
            new Thread(server).start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

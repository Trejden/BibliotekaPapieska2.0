package Server;

public class DatabaseConfig {
    public static String DataBaseName = "BibliotekaPapieska";
    public static int DataBasePort = 3306;
    public static String DataBaseAddress = "localhost";
    public static class UsersDataModel{
        public static String TableName = "Users";
        public static String UserRole = "UserRole";
        public static String UserName = "UserName";
        public static String Password = "Password";
        public static String Id = "Id";
        public static String Mail = "Mail";
        public static String GetSqlCreationString() {
            return "Create Table " + TableName + " ("
                    + Id + " int AUTO_INCREMENT PRIMARY KEY, "
                    + UserName + " varchar(255) NOT NULL, "
                    + UserRole + " int NOT NULL, "
                    + Password + " varchar(255) NOT NULL, "
                    + Mail + " varchar(255)"
                    +");";
        }
        public static String GetInsertStatementString(String userName, int role, String password, String mail){
            return "INSERT INTO " + TableName + " (" + UserName + ", " + UserRole + ", " + Password + ", " + Mail + ")"
                    + " VALUES ("
                    + "'" + userName + "', "
                    + "'" + role + "', "
                    + "'" + password + "', "
                    + "'" + mail + "');";
        }
    }
    public static class ReservationsDataModel {
        public static String TableName = "Reservations";
        public static String Id = "Id";
        public static String GetSqlCreationString(){
            return "Create Table " + TableName + " ("
                    + Id + " int AUTO_INCREMENT PRIMARY KEY"
                    + ");";
        }
    }
    public static class RentalsDataModel {
        public static String TableName = "Rentals";
        public static String Id = "Id";
        public static String GetSqlCreationString(){
            return "Create Table " + TableName + " ("
                    + Id + " int AUTO_INCREMENT PRIMARY KEY"
                    + ");";
        }
    }
    public static class RentablesDataModel {
        public static String TableName = "Rentable";
        public static String Id = "Id";
        public static String GetSqlCreationString(){
            return "Create Table " + TableName + " ("
                    + Id + " int AUTO_INCREMENT PRIMARY KEY"
                    + ");";
        }
    }
    public static class PenaltiesDataModel {
        public static String TableName = "Penalties";
        public static String Id = "Id";
        public static String GetSqlCreationString(){
            return "Create Table " + TableName + " ("
                    + Id + " int AUTO_INCREMENT PRIMARY KEY"
                    + ");";
        }
    }
}

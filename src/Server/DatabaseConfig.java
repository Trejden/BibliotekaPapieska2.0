package Server;

import java.util.Objects;

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
        public static String GetSelectStatementByUserName(String userName) {
            return "SELECT" + " " + UserName + ", " + UserRole + ", " + Password + ", " + Mail
                    + " FROM " + TableName
                    + " WHERE " + UserName + " = '" + userName + "';";
        }
        public static String GetSelectStatementAll(){
            return "SELECT" + " " + UserName + ", " + UserRole + ", " + Password + ", " + Mail
                    + " FROM " + TableName + "';";
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
        public static String IBAN = "IBAN";
        public static String Author = "Author";
        public static String Title = "Title";
        public static String GetSqlCreationString(){
            return "Create Table " + TableName + " ("
                    + Id + " int AUTO_INCREMENT PRIMARY KEY, "
                    + IBAN + " varchar(255) NOT NULL, "
                    + Author + " varchar(255) NOT NULL, "
                    + Title + " varchar(255) NOT NULL, "
                    + ");";
        }
        public static String GetSqlInsertString(String iban, String author, String title){
            return "INSERT INTO " + TableName + " (" + IBAN + ", " + Author + ", " + Title + ")"
                    + " VALUES ("
                    + "'" + iban + "', "
                    + "'" + author + "', "
                    + "'" + title + "');";
        }
        public static String GetSelectStatementAll() {
            return "SELECT" + " " + IBAN + ", " + Author + ", " + Title
                    + " FROM " + TableName + "';";
        }
        public static String GetSelectStatementFiltered(String iban, String author, String title){
            String sql = "SELECT" + " " + IBAN + ", " + Author + ", " + Title
                    + " FROM " + TableName;
            if(!Objects.equals(iban, "") || !Objects.equals(author, "") || !Objects.equals(title, "")){
                sql = sql + " WHERE ";
            }
            if(!Objects.equals(iban, "")){
                sql = sql + IBAN + " = '" + iban + "'";
            }
            if(!Objects.equals(author, "")){
                if(!Objects.equals(iban, "")){
                    sql = sql + " AND ";
                }
                sql = sql + Author + " = '" + author + "'";
            }
            if(!Objects.equals(title, "")){
                if(!Objects.equals(author, "")){
                    sql = sql + " AND ";
                }
                if(!Objects.equals(iban, "") && Objects.equals(author, "")){
                    sql = sql + " AND ";
                }
                sql = sql + Title + " = '" + title + "'";
            }

            return sql + ";";
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

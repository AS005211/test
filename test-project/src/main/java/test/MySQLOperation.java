package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MySQLOperation {
    private String url;
    private String database;
    private String sqlUser;
    private String sqlPassword;

    public MySQLOperation(String url, String sqlUser, String sqlPassword) {
        this.url = "jdbc:mysql://localhost:3306/"+url+"?UseSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.database = url;
        this.sqlUser = sqlUser;
        this.sqlPassword = sqlPassword;
    }

    public void insertData(ArrayList<String> filesNames, ArrayList<String> filesDates){
        String query;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection(url,sqlUser,sqlPassword);
            statement = connection.createStatement();
            for(int i=0; i<filesNames.size();i++){
                query = "INSERT INTO "+database+".files (id_file, file_name, file_date) \n" +
                        "VALUES ("+(i+1)+", '"+filesNames.get(i)+"', '"+filesDates.get(i)+"');";
                statement.executeUpdate(query);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
            connection.close();
            statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public void selectData(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT file_name, file_date FROM files";
        try {
            connection = DriverManager.getConnection(url,sqlUser,sqlPassword);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                System.out.println("File name: " + resultSet.getString(1) + "; File was copied: " + resultSet.getString(2));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
}

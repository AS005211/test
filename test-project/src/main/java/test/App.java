package test;


import net.schmizz.sshj.sftp.RemoteResourceInfo;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


public class App 
{
    public static void main( String[] args ) throws IOException {


        System.out.println("Please enter configuration file path");
        Scanner scanner = new Scanner(System.in);
        String cfgFile = scanner.nextLine();

        try (FileInputStream inputStream = new FileInputStream(cfgFile)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            String remoteHost = properties.getProperty("sftp_host");
            String sftpUser = properties.getProperty("sftp_user");
            String sftpPassword = properties.getProperty("sftp_password");
            String remoteDir = properties.getProperty("sftp_remote_dir");
            String localDir = properties.getProperty("local_dir");
            String sqlDatabase = properties.getProperty("sql_database");
            String sqlUser = properties.getProperty("sql_user");
            String sqlPassword = properties.getProperty("sql_password");

            SftpOperation mySftp = new SftpOperation(remoteHost,sftpUser,sftpPassword,remoteDir,localDir);
            List<RemoteResourceInfo> filesInfo = mySftp.copyFiles();
            ArrayList<String> fileNames = mySftp.getFilesNames(filesInfo);
            ArrayList<String> fileDates = mySftp.getFilesCopyTime(filesInfo);

            MySQLOperation mySQLOperation = new MySQLOperation(sqlDatabase,sqlUser,sqlPassword);
            mySQLOperation.insertData(fileNames,fileDates);
            mySQLOperation.selectData();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

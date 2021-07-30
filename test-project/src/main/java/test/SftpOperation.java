package test;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SftpOperation {
    private String host;
    private String sftpUser;
    private String sftpPassword;
    private String remoteDir;
    private String localDir;

    public SftpOperation(String host, String sftpUser, String sftpPassword, String remoteDir, String localDir) {
        this.host = host;
        this.sftpUser = sftpUser;
        this.sftpPassword = sftpPassword;
        this.remoteDir = remoteDir;
        this.localDir = localDir;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSftpUser() {
        return sftpUser;
    }

    public void setSftpUser(String sftpUser) {
        this.sftpUser = sftpUser;
    }

    public String getSftpPassword() {
        return sftpPassword;
    }

    public void setSftpPassword(String sftpPassword) {
        this.sftpPassword = sftpPassword;
    }

    public String getRemoteDir() {
        return remoteDir;
    }

    public void setRemoteDir(String remoteDir) {
        this.remoteDir = remoteDir;
    }

    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    public List<RemoteResourceInfo> copyFiles(){
        List<RemoteResourceInfo> filesInfo = null;
        try {
            SSHClient client = new SSHClient();
            client.addHostKeyVerifier(new PromiscuousVerifier());
            client.connect(host);
            client.authPassword(sftpUser,sftpPassword);
            SFTPClient sftpClient = client.newSFTPClient();
            filesInfo =  sftpClient.ls(remoteDir);
           for(RemoteResourceInfo s : filesInfo){
                sftpClient.get(s.getPath(),localDir);
            }
            sftpClient.close();
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filesInfo;
    }

    public ArrayList<String> getFilesNames(List<RemoteResourceInfo> filesInfo){
        ArrayList<String> fileNames = new ArrayList<>();
        for(RemoteResourceInfo s : filesInfo){
            fileNames.add(s.getName());
        }

        return fileNames;
    }

    public ArrayList<String> getFilesCopyTime(List<RemoteResourceInfo> filesInfo){
        ArrayList<String> fileTime = new ArrayList<>();
        for(RemoteResourceInfo s : filesInfo){
            try {
                BasicFileAttributes attributes = Files.readAttributes(Paths.get(localDir + "/" + s.getName()),BasicFileAttributes.class);
                fileTime.add(attributes.creationTime().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileTime;
    }
}

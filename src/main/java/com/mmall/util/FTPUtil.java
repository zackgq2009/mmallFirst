package com.mmall.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    /**
     * ftp.server.ip=你的FTP服务器ip地址
     * ftp.user=ftpuser
     * ftp.pass=123456
     */
    private static final String FTPIP = PropertiesUtil.getProperty("ftp.server.ip", "127.0.0.1");
    private static final String FTPUserName = PropertiesUtil.getProperty("ftp.user", "admin");
    private static final String FTPPassword = PropertiesUtil.getProperty("ftp.pass", "password");
    private static final int FTPPort = 21;

    private String IP;
    private int port;
    private String userName;
    private String password;
    private FTPClient ftpClient;

    public FTPUtil() {
    }

    public FTPUtil(String IP, int port, String userName, String password) {
        this.IP = IP;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public static boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(FTPIP, FTPPort, FTPUserName, FTPPassword);
        logger.info("开始上传文件");
        boolean result = ftpUtil.upload(remotePath, fileList);
        logger.info("结束上传文件");
        return result;
    }

    //把上传文件的具体业务进行封装
    private boolean upload(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = false;
        FileInputStream fileInputStream = null;

        //先连接服务器
        if (connectFTP(this.IP, this.port, this.userName, this.password)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //接下来设备FTP服务器的主动，被动模式，根据自己FTP服务器的模式
                ftpClient.enterLocalPassiveMode();
                for (File item : fileList
                        ) {
                    fileInputStream = new FileInputStream(item);
                    //storeFile方法是上传文件，需要传入remote以及fis
                    uploaded = ftpClient.storeFile(item.getName(), fileInputStream);
                }
            } catch (IOException e) {
                logger.error("上传文件有异常", e);
                uploaded = false;
            } finally {
                //关闭所有的IO
                fileInputStream.close();
                ftpClient.disconnect();
//                return uploaded;
            }
        }
        return uploaded;
    }

    //把连接FTP服务器进行封装
    private boolean connectFTP(String ip, int port, String userName, String password) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(userName, password);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
            isSuccess = false;
        } finally {
            return isSuccess;
        }
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}

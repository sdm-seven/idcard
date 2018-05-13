package com.ych.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Config {
    private static int dbflag = 2;
    private static boolean debug = true;
    // oracle
    private static String oracle_driver = "oracle.jdbc.driver.OracleDriver";
    private static String oracle_url = "jdbc:oracle:thin:@111.173.78.2:1522:govsy";
    private static String oracle_user = "lc_jcyw";
    private static String oracle_pwd = "lc_jcyw";
    // XTAYgyiy321

    // mysql
    private static String mysql_driver = "com.mysql.jdbc.Driver";
    // private static String mysql_url =
    // "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8";
    private static String mysql_url = "jdbc:mysql:loadbalance://120.78.151.83:3306/blog?roundRobinLoadBalance=true&useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static String mysql_user = "mysqlych";
    private static String mysql_pwd = "Mysqlych201818!";

    // sqlserver
    private static String mssql_driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String mssql_url = "jdbc:sqlserver://127.0.0.1:1433;databaseName=GuoXin;integratedSecurity=false;PersistSecurityInfo=true";
    private static String mssql_user = "sa";
    private static String mssql_pwd = "";

    private static int maxConn = 300;
    private static int minConn = 5;

    // sqls
    private static Properties properties = new Properties();
    static {
        InputStream inStream = null;
        if (dbflag == 1) {
            inStream = Config.class.getResourceAsStream("oracle.properties");
        } else if (dbflag == 2) {
            inStream = Config.class.getResourceAsStream("mysql.properties");
        } else if (dbflag == 3) {
            inStream = Config.class.getResourceAsStream("mssql.properties");
        }
        try {
            if (inStream != null) {
                properties.load(inStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String uploadDirName = "D:\\upload";
    private static long uploadMaxSize = 5 * 1024 * 1024;
    private static String logDirName = "D:\\logs";

    public static OutputStream getWriter() {
        OutputStream os = null;
        try {
            String time = new SimpleDateFormat("yyyyMMddHH").format(new Date(System.currentTimeMillis()));
            String path = Config.getLogDirName() + "\\" + time.substring(0, 8) + "\\";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            os = new FileOutputStream(new File(path + time.substring(8)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return os;
    }

    public static String getSql(String key) {
        return properties.getProperty(key);
    }

    public static boolean interceptor(HttpServletRequest req, HttpServletResponse res) {

        return false;
    }

    public static int getDbflag() {
        return dbflag;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static String getOracle_driver() {
        return oracle_driver;
    }

    public static String getOracle_url() {
        return oracle_url;
    }

    public static String getOracle_user() {
        return oracle_user;
    }

    public static String getOracle_pwd() {
        return oracle_pwd;
    }

    public static String getMysql_driver() {
        return mysql_driver;
    }

    public static String getMysql_url() {
        return mysql_url;
    }

    public static String getMysql_user() {
        return mysql_user;
    }

    public static String getMysql_pwd() {
        return mysql_pwd;
    }

    public static String getMssql_driver() {
        return mssql_driver;
    }

    public static String getMssql_url() {
        return mssql_url;
    }

    public static String getMssql_user() {
        return mssql_user;
    }

    public static String getMssql_pwd() {
        return mssql_pwd;
    }

    public static int getMaxConn() {
        return maxConn;
    }

    public static int getMinConn() {
        return minConn;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static String getUploadDirName() {
        return uploadDirName;
    }

    public static String getLogDirName() {
        return logDirName;
    }

    public static long getUploadMaxSize() {
        return uploadMaxSize;
    }

    public static PublicKey readPublicKey() {
        return null;
    }

    public static PrivateKey readPrivateKey() {
        return null;
    }
}
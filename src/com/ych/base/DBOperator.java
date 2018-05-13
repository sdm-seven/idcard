package com.ych.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBOperator {
    private final String        driver;
    private final String        url;
    private final String        user;
    private final String        password;
    
    private List<Connection>    connPool = new ArrayList<>();
    private final int           maxConn;
    private final int           minConn;
    private volatile int        count    = 0;
    
    private static final String TAG      = DBOperator.class.getName();
    
    public DBOperator(String driver, String url, String user, String password,
            int maxConn, int minConn) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        
        this.maxConn = maxConn;
        this.minConn = minConn;
    }
    
    public DBOperator(String driver, String url, String user, String password) {
        this(driver, url, user, password, Config.getMaxConn(), Config
                .getMinConn());
    }
    
    @SuppressWarnings("resource")
    private synchronized Connection getConnection() {
        if (connPool.size() <= 0) {
            return createConnection();
        } else {
            Connection conn = connPool.remove(0);
            try {
                int tryTime = 0;
                while (conn == null || conn.isClosed()) {
                    if (tryTime++ >= 50) {
                        conn = createConnection();
                        break;
                    }
                    if (connPool.size() <= 0) {
                        conn = createConnection();
                    } else {
                        conn = connPool.remove(0);
                    }
                    Thread.sleep(500);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return conn;
        }
    }
    
    private synchronized Connection createConnection() {
        Connection conn = null;
        if (count >= maxConn) {
            return null;
        }
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        count++;
        return conn;
    }
    
    public synchronized void close(Connection conn) {
        try {
            if (conn == null || conn.isClosed()) {
                return;
            }
            if (connPool.size() < minConn) {
                connPool.add(conn);
            } else {
                conn.close();
                count--;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized void close(Statement st) {
        try {
            if (st == null || st.isClosed()) {
                return;
            }
            st.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized void close(ResultSet rs) {
        try {
            if (rs == null || rs.isClosed()) {
                return;
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public int[] execSqls(String[] sqls, Object[][] paramss) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            int cnt = sqls.length;
            int[] ret = new int[cnt];
            String sql = null;
            for (int i = 0; i < cnt; i++) {
                sql = sqls[i];
                ps = conn.prepareStatement(sql);
                if (!Tools.isEmpty(paramss)) {
                    setParams(ps, paramss[i]);
                }
                ret[i] = ps.executeUpdate();
            }
            conn.commit();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            close(ps);
            close(conn);
        }
        return null;
    }
    
    public int[] execBatch(String sql, Object[][] paramss) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            if (!Tools.isEmpty(paramss)) {
                for (Object[] params : paramss) {
                    setParams(ps, params);
                    ps.addBatch();
                }
            }
            int[] ret = ps.executeBatch();
            conn.commit();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            close(ps);
            close(conn);
        }
        return null;
    }
    
    // insert,update,delete batch
    public int exec(String sql, Object[] params) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            setParams(ps, params);
            int ret = ps.executeUpdate();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }
        return -1;
    }
    
    // select ,columns = * || a,b
    public JSONArray get(String sql, Object[] params) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            setParams(ps, params);
            rs = ps.executeQuery();
            JSONObject jo = null;
            ResultSetMetaData rsmd = rs.getMetaData();
            int cnt = rsmd.getColumnCount();
            String temp = null;
            JSONArray ja = new JSONArray();
            while (rs.next()) {
                jo = new JSONObject();
                for (int i = 1; i <= cnt; i++) {
                    temp = rsmd.getColumnLabel(i);
                    jo.put(temp, rs.getString(i));
                }
                ja.put(jo);
            }
            
            return ja;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }
        return null;
    }
    
    private void setParams(PreparedStatement ps, Object[] params)
            throws SQLException {
        if (Tools.isEmpty(params)) {
            return;
        }
        int len = params.length;
        Object param = null;
        
        for (int i = 1; i <= len; i++) {
            param = params[i - 1];
            if (param instanceof String) {
                ps.setString(i, (String) param);
            } else if (param instanceof Integer) {
                ps.setInt(i, ((Integer) param).intValue());
            } else if (param instanceof Double) {
                ps.setDouble(i, ((Double) param).doubleValue());
            } else if (param instanceof Long) {
                ps.setLong(i, ((Long) param).longValue());
            } else if (param instanceof Float) {
                ps.setFloat(i, ((Float) param).floatValue());
            }
        }
    }
}

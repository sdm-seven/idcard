package com.ych.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;

public class Tools {
	private static final String TAG = Tools.class.getName();

	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		} else if (o instanceof String) {
			return o.toString().trim().equals("");
		} else if (JSONArray.class.isAssignableFrom(o.getClass())) {
			return ((JSONArray) o).length() <= 0;
		} else if (Collection.class.isAssignableFrom(o.getClass())) {
			return ((Collection<?>) o).size() <= 0;
		} else if (Map.class.isAssignableFrom(o.getClass())) {
			return ((Map<?, ?>) o).size() <= 0;
		}
		return false;
	}

	public static final DBOperator getDBOInstance() {
		return getDBOInstance(Config.getDbflag());
	}

	public static final DBOperator getDBOInstance(int dbflag) {
		if (dbflag < 1 || dbflag > 3) {
			return null;
		}
		if (dbflag == 1) {
			return new OracleDBO();
		} else if (dbflag == 2) {
			return new MysqlDBO();
		} else {
			return new MssqlDBO();
		}
	}

	public static final byte[] getBytes(String src, String encoding) {
		try {
			return src.getBytes(encoding);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return src.getBytes();
	}

	public static final String getString(byte[] src, String encoding) {
		try {
			return new String(src, encoding);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return new String(src);
	}

	public static final byte[] getBytes(String src) {
		return getBytes(src, "UTF-8");
	}

	public static final String getString(byte[] src) {
		return getString(src, "UTF-8");
	}

	public static final String reverse(String src) {
		return new StringBuffer(src).reverse().toString();
	}

	public static final String getString(HttpServletRequest request, String key) {
		return getString(request, key, "");
	}

	public static final String getString(HttpServletRequest request, String key, String dflt) {
		String temp = request.getParameter(key);
		if (Tools.isEmpty(temp)) {
			return dflt;
		}
		return temp;
	}

	public static final Integer getInteger(HttpServletRequest request, String key) {
		return getInteger(request, key, Integer.valueOf(0));
	}

	public static final Integer getInteger(HttpServletRequest request, String key, Integer dflt) {
		String temp = getString(request, key);
		if (Tools.isEmpty(temp)) {
			return dflt;
		}
		try {
			return Integer.valueOf(temp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dflt;

	}

	public static final Double getDouble(HttpServletRequest request, String key) {
		return getDouble(request, key, 0D);
	}

	public static final Double getDouble(HttpServletRequest request, String key, Double dflt) {
		String temp = getString(request, key);
		if (Tools.isEmpty(temp)) {
			return dflt;
		}
		try {
			return Double.valueOf(temp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dflt;
	}

	public static final String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip != null && ip.length() > 0 && !"unKnown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (ip != null && ip.length() > 0 && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	public static final String[] getParams(HttpServletRequest request) {
		String[] params = null;

		try {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(Config.getUploadMaxSize());// 5M
			long current = System.currentTimeMillis();
			String time = new SimpleDateFormat("yyyyMMdd").format(new Date(current));
			String path = Config.getUploadDirName() + "\\" + time + "\\";
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			List<FileItem> list;
			list = upload.parseRequest(new RequestContextUtil(request));
			int len = list.size();
			params = new String[len];
			for (int i = len; i-- > 0;) {
				FileItem item = list.get(i);
				if (item.isFormField()) {
					list.remove(item);
					// String name = item.getFieldName();
					String value = item.getString("utf-8");
					params[i] = value;
				} else {
					String fileName = item.getName().replaceAll("\\s", "");
					int pointPos = fileName.lastIndexOf(".");
					pointPos = fileName.lastIndexOf(".");
					fileName = fileName.substring(0, pointPos) + "_" + current + fileName.substring(pointPos);
					params[i] = path + fileName;
					item.write(new File(params[i]));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}
}
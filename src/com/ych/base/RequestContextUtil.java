package com.ych.base;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletRequest;
import org.apache.tomcat.util.http.fileupload.UploadContext;

public class RequestContextUtil implements UploadContext {
    private ServletRequest mRequest;
    
    public RequestContextUtil(ServletRequest request) {
        mRequest = request;
    }
    
    @Override
    public String getCharacterEncoding() {
        // TODO Auto-generated method stub
        return mRequest.getCharacterEncoding();
    }
    
    @Override
    public String getContentType() {
        // TODO Auto-generated method stub
        return mRequest.getContentType();
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        // TODO Auto-generated method stub
        return mRequest.getInputStream();
    }
    
    @Override
    public long contentLength() {
        // TODO Auto-generated method stub
        return mRequest.getContentLengthLong();
    }
}

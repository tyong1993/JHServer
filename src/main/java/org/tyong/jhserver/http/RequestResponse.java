package org.tyong.jhserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RequestResponse {
    protected Socket socket = null;
    protected InputStream inputStream = null;
    protected OutputStream outputStream = null;

    protected String url;

    protected String response_code = "200";
    protected String response_msg = "ok";
    protected HashMap<String,String> header = new HashMap<String, String>();

    public RequestResponse(Socket socket) throws Exception {
        this.socket = socket;
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
            this.init();
        } catch (Exception e) {
            if(null != this.inputStream){
                this.inputStream.close();
            }
            if(null != this.outputStream){
                this.outputStream.close();
            }
            throw e;

        }
        this.header.put("Content-Type","text/html;charset=utf-8\n");
    }

    private void init() throws Exception {
        byte[] inDataBox = new byte[2048];
        int inDataLength = 0;
        inDataLength = this.inputStream.read(inDataBox);
        if(inDataLength < 0){
            throw new Exception("can not get request data");
        }
        byte[] inData = Arrays.copyOf(inDataBox, inDataLength);
        String inDataStr = new String(inData);
        //获取请求字符串
        String url = null;
        int index1 = inDataStr.indexOf(" ");
        if(index1 != -1){
            int index2 = inDataStr.indexOf(" ",index1+1);
            if(index2 > index1){
                url = inDataStr.substring(index1,index2);
            }
        }
        if(null == url){
            throw new Exception("not a right http");
        }
        this.url = url;
    }
    public void sendResponse(String content) throws IOException {
        try {
            this.outputStream.write(("HTTP/1.1 "+this.response_code+" "+this.response_msg+"\n").getBytes());
            Set<String> headers_key = this.header.keySet();
            for(Iterator<String> iterator = headers_key.iterator(); iterator.hasNext();){
                String key = iterator.next();
                this.outputStream.write((key+":"+this.header.get(key)+"\n").getBytes());
            }
            this.outputStream.write("\n".getBytes());
            this.outputStream.write(content.getBytes());
            this.outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != this.inputStream){
                this.inputStream.close();
            }
            if(null != this.outputStream){
                this.outputStream.close();
            }
        }
    }

    public String getUrl() {
        return this.url;
    }

    public void setResponseCode(String code) {
        this.response_code = code;
    }

    public void setResponseMsg(String msg) {
        this.response_msg = msg;
    }

    public void setResponseHeader(String key, String val) {
        if(key == "Content-Type"){
            this.header.put("Content-Type",val+";charset=utf-8\n");
        }else {
            this.header.put(key,val+"\n");
        }
    }
}

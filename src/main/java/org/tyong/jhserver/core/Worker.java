package org.tyong.jhserver.core;

import org.tyong.jhserver.http.*;

import java.io.IOException;
import java.net.Socket;

/**
 * 工作线程
 */
public class Worker implements Runnable {
    /**
     * 初始化工作线程
     */
    public void init(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("worker start work...");
        while (true){
            Socket socket;
            synchronized (this) {
                if(Connector.connectPool.size() == 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                socket = Connector.connectPool.remove(0);
            }
            RequestResponse requestResponse = null;
            try {
                requestResponse = new RequestResponse(socket);
                System.out.println(requestResponse.toString());
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            //请求url
            String url = requestResponse.getUrl();
            //处理业务逻辑
            //todo
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //响应输出
            requestResponse.setResponseCode("200");
            try {
                requestResponse.sendResponse("200");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

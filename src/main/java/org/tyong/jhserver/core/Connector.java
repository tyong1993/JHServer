package org.tyong.jhserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 连接器
 */
public class Connector extends Thread {
    /**
     * 连接池
     */
    public static volatile LinkedBlockingDeque<Socket> connectPool = new LinkedBlockingDeque<Socket>();
    /**
     * 初始化连接器
     */
    public void init() {
        this.start();
    }
    @Override
    public void run() {
        System.out.println("Connector start work...");
        Socket accept = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true){
                accept = serverSocket.accept();
                connectPool.put(accept);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(null!= accept){
                try {
                    accept.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

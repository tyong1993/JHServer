package org.tyong.jhserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 连接器
 */
public class Connector implements Runnable {
    /**
     * 连接池
     */
    public static ArrayList<Socket> connectPool = new ArrayList<Socket>();

    /**
     * 初始化连接器
     */
    public void init() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        System.out.println("Connector start work...");
        Socket accept = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true){
                accept = serverSocket.accept();
                connectPool.add(accept);
            }
        } catch (IOException e) {
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

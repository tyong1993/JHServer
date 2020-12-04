package org.tyong.jhserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 连接器
 */
public class Connector implements Runnable {
    /**
     * 连接池
     */
    public static ArrayList<Socket> connectPool = new ArrayList<Socket>();
    public void init() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            this.doWork();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void doWork() throws IOException {
        System.out.println("Connector start work...");
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true){
            Socket accept = serverSocket.accept();
            System.out.println("client connected:"+accept.getInetAddress()+":"+System.currentTimeMillis());

            InputStream inputStream = accept.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            int i;
            while ((i = inputStreamReader.read()) != -1) {
                System.out.print((char) i);
            }
            System.out.print("read complete");
            //accept.close();
//            connectPool.add(accept);
        }
    }
}

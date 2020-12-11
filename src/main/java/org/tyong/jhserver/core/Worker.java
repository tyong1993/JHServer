package org.tyong.jhserver.core;

import org.tyong.jhserver.http.*;

import java.net.Socket;

/**
 * 工作线程
 */
public class Worker extends Thread {
    public volatile boolean is_active = true;
    @Override
    public void run() {
        while (!this.isInterrupted()){
            Socket socket;
            synchronized (this) {
                socket = Connector.connectPool.poll();
                if(null == socket){
                    try {
                        WorkerManager.setActiveWorkerNumDec();
                        this.is_active = false;
                        this.wait();
                        this.is_active = true;
                        WorkerManager.setActiveWorkerNumInc();
                    } catch (InterruptedException e) {
                        break;
                    }
                    continue;
                }
            }

            RequestResponse requestResponse = null;
            try {
                requestResponse = new RequestResponse(socket);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            DispatcherHandler handler = WorkerManager.container.getHandler();
            handler.doRequest(requestResponse);
        }
    }
}

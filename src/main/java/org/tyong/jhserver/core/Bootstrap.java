package org.tyong.jhserver.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 启动器
 */
public class Bootstrap {
    public static void main(String[] args) throws Exception {
        System.out.println("jhserver start......");
        Connector connector = new Connector();
        connector.init();
        WorkerManager workerManager = new WorkerManager();
        workerManager.init();
        System.out.println("jhserver start successful");

        while (true){
            Thread.sleep(1000);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("待处理请求个数:"+Connector.connectPool.size()+"   ");
            stringBuilder.append("worker总个数:"+WorkerManager.workerPoolNum+"   "+WorkerManager.workerPool.size()+"   ");
            stringBuilder.append("工作中的worker个数:"+WorkerManager.activeWorkerNum);
            System.out.println(stringBuilder.toString());
        }
    }
}

package org.tyong.jhserver.core;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 启动器
 */
public class Bootstrap {
    public static void main(String[] args) throws IOException {
        System.out.println("jhserver start......");
        Connector connector = new Connector();
        connector.init();
        Worker worker = new Worker();
        worker.init();
        worker.init();
        worker.init();
        System.out.println("jhserver start successful");
    }
}

package org.tyong.jhserver.core;

import org.tyong.jhserver.controller.Controller;

import java.util.HashMap;

/**
 * 处理器容器
 */
public class Container {
    private DispatcherHandler handler;

    private HashMap<String,Controller> controllerMap = new HashMap<String, Controller>();
    public void init(){
        System.out.println("Container init...");
        this.handler = new DispatcherHandler();
    }
    public DispatcherHandler getHandler(){
        return this.handler;
    }

    public synchronized Controller getController(String controllerName){
        String first = controllerName.substring(0,1);
        String s = first.toUpperCase();
        controllerName = s+controllerName.substring(1);
        Controller controller = this.controllerMap.get(controllerName);
        if(null == controller){
            //尝试加载
            try {
                Class<?> aClass = Class.forName("org.tyong.jhserver.controller." + controllerName);
                controller = (Controller) aClass.newInstance();
                this.controllerMap.put(controllerName,controller);
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
        return controller;
    }

}

package org.tyong.jhserver.core;

import org.tyong.jhserver.controller.Controller;
import org.tyong.jhserver.http.RequestResponse;

import java.io.IOException;
import java.util.Arrays;

/**
 * 处理器
 */
public class DispatcherHandler {
    public void doRequest(RequestResponse requestResponse){
        String url = requestResponse.getUrl();
        String[] split = url.split("/");
        if(split.length != 2){
            requestResponse.setResponseCode("404");
            try {
                requestResponse.sendResponse("请求格式错误");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            String controller = split[1];
            int i = controller.indexOf(".do");
            if(i == -1 || controller.length() != i+3){
                requestResponse.setResponseCode("404");
                try {
                    requestResponse.sendResponse("请求必须以.do结尾");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                controller = controller.substring(0,i);
                Controller controller1 = WorkerManager.container.getController(controller);
                if(null == controller1){
                    requestResponse.setResponseCode("404");
                    try {
                        requestResponse.sendResponse("请求的服务不存在");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    String res = controller1.service();
                    try {
                        requestResponse.sendResponse(res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

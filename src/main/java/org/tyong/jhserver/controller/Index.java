package org.tyong.jhserver.controller;

public class Index implements Controller {
    @Override
    public String service() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "首页";
    }
}

package org.tyong.jhserver.controller;

public class User implements Controller {
    @Override
    public String service() {
        return "user";
    }
}

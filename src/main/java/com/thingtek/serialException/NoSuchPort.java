package com.thingtek.serialException;

public class NoSuchPort extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NoSuchPort() {
    }

    @Override
    public String toString() {
        return "没有找到与该端口名匹配的串口设备！请检查设备连接或重新设置串口";
    }

}

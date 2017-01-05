package com.sino.netty.pojo;

import java.util.Date;

/**
 * Created by thierry.fu on 2017/1/5.
 */
public class UnixTime {

    private final int value;

    public UnixTime(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new Date(value * 1000L).toString();
    }
}

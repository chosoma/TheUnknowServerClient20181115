package com.thingtek.com.entity;

import com.thingtek.view.logo.LogoInfo;

public abstract class BaseG2S extends BaseP2P {

    protected byte[] bytes;

    public BaseG2S setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    protected LogoInfo logoInfo;

    public void setLogoInfo(LogoInfo logoInfo) {
        this.logoInfo = logoInfo;
    }



    @Override
    public void resolve() {

    }

    public byte[] getResult() {
        return new byte[0];
    }

}
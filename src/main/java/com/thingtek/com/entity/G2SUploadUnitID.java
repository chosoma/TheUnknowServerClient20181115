package com.thingtek.com.entity;

public class G2SUploadUnitID extends BaseG2S {


    @Override
    public void resolve() {
        int dataLength = agreementConfig.getDataLength(0, bytes);
        int parameteroff = agreementConfig.getParameteroff();
        byte[] data = new byte[dataLength];
        System.arraycopy(bytes, parameteroff, data, 0, data.length);

    }

    @Override
    public byte[] getResult() {
        return new byte[0];
    }

}
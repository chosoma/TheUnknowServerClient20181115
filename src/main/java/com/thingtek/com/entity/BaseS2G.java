package com.thingtek.com.entity;

public abstract class BaseS2G extends BaseP2P {

    protected byte[] cmdtype;

    public void setCmdtype(byte[] cmdtype) {
        this.cmdtype = cmdtype;
    }

    protected byte[] seq;

    protected byte[] ordertype;

    protected byte[] datalength;

    protected byte[] datas;

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    @Override
    public void resolve() {
        seq = new byte[agreementConfig.getSeq().length];
    }


    public byte[] getResult() {
        if (datas == null) {
            datas = new byte[0];
        }
        ordertype = agreementConfig.getOrderrequest();
        datalength = int2bytes(datas.length, agreementConfig.getDatalength().length);
        byte[] bytes = new byte[agreementConfig.getTotallength() + datas.length];
        System.arraycopy(agreementConfig.getHead(), 0, bytes, agreementConfig.getHeadoff(), agreementConfig.getHead().length);
        System.arraycopy(seq, 0, bytes, agreementConfig.getSeqoff(), seq.length);
        System.arraycopy(ordertype, 0, bytes, agreementConfig.getOrdertypeoff(), ordertype.length);
        System.arraycopy(cmdtype, 0, bytes, agreementConfig.getCmdtypeoff(), cmdtype.length);
        System.arraycopy(datalength, 0, bytes, agreementConfig.getDatalengthoff(), datalength.length);
        System.arraycopy(datas, 0, bytes, agreementConfig.getParameteroff(), datas.length);
        return bytes;
    }

}

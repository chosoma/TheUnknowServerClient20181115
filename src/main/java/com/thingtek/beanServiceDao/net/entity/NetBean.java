package com.thingtek.beanServiceDao.net.entity;

import lombok.Data;

public @Data
class NetBean {

    private Integer id;
    private Byte net_type;
    private Byte net_num;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NetBean netBean = (NetBean) o;

        return (net_type != null ? net_type.equals(netBean.net_type) : netBean.net_type == null)
                && (net_num != null ? net_num.equals(netBean.net_num) : netBean.net_num == null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (net_type != null ? net_type.hashCode() : 0);
        result = 31 * result + (net_num != null ? net_num.hashCode() : 0);
        return result;
    }

    public byte[] getsendByte() {
        return new byte[]{net_type, net_num};
    }

}

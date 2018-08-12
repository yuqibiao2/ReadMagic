package com.arfid.readmagic.bean;

/**
 * Created by admin on 2017/1/10.
 */
public class EPCBean {

    private String epc ;
    private int count ;

    public EPCBean(String epc, int count) {
        this.epc = epc;
        this.count = count;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

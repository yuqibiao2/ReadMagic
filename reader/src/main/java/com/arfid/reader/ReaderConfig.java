package com.arfid.reader;

import com.thingmagic.Gen2;

/**
 * 读写器参数配置
 *
 * @author yu
 */
public class ReaderConfig {

    //天线
    private int[] antennaList = {1, 2, 3, 4};
    //读写器读的功率（0~3150 默认值3000）
    private int readPower = 3000;
    //波特率
    private int baudRate;

    private Gen2.LinkFrequency blf = Gen2.LinkFrequency.LINK320KHZ;
    private Gen2.Tari tari = Gen2.Tari.TARI_6_25US;
    private Gen2.TagEncoding tagEncoding = Gen2.TagEncoding.FM0;
    private Gen2.Target target = Gen2.Target.A;
    private Gen2.Session session = Gen2.Session.S1;

    public int[] getAntennaList() {
        return antennaList;
    }

    public void setAntennaList(int[] antennaList) {
        this.antennaList = antennaList;
    }

    public int getReadPower() {
        return readPower;
    }

    public void setReadPower(int readPower) {
        this.readPower = readPower;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public Gen2.LinkFrequency getBlf() {
        return blf;
    }

    public void setBlf(Gen2.LinkFrequency blf) {
        this.blf = blf;
    }

    public Gen2.Tari getTari() {
        return tari;
    }

    public void setTari(Gen2.Tari tari) {
        this.tari = tari;
    }

    public Gen2.TagEncoding getTagEncoding() {
        return tagEncoding;
    }

    public void setTagEncoding(Gen2.TagEncoding tagEncoding) {
        this.tagEncoding = tagEncoding;
    }

    public Gen2.Target getTarget() {
        return target;
    }

    public void setTarget(Gen2.Target target) {
        this.target = target;
    }

    public Gen2.Session getSession() {
        return session;
    }

    public void setSession(Gen2.Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "antennaList："+antennaList+
                "readPower："+readPower+
                "blf："+blf+
                "tari："+tari+
                "tagEncoding："+tagEncoding+
                "target："+target+
                "session："+session;
    }
}

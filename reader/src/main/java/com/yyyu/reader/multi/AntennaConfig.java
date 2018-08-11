package com.yyyu.reader.multi;

import java.util.Arrays;
import java.util.List;

/**
 * 天线参数配置封装bean
 *
 * @author yu
 *
 */
public class AntennaConfig {

   //天线
    private int[] antennaList;
    //读写器读的功率（默认值3000）
    private int readPower = 3000;
    //波特率
    private int baudRate;
    private int blf;
    private int tari;
    private int tagCoding;
    private int target;
    private int session;
    private boolean isReadByTurn = false;
    //private long readInterval = 5*1000;
    private List<Long> readIntervalList ;
    private int sendDataModel = 0;
    private long sendDataInterval=15;
    private boolean isAlternateTargetA_B;
    private long targetAlternateInterval = 1000;


    private static AntennaConfig instance = new AntennaConfig();

    private AntennaConfig(){

    }

    public static AntennaConfig getInstance(){
        if (instance == null){
            instance = new AntennaConfig();
        }

        return instance;
    }

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

    public int getBlf() {
        return blf;
    }

    public void setBlf(int blf) {
        this.blf = blf;
    }

    public int getTari() {
        return tari;
    }

    public void setTari(int tari) {
        this.tari = tari;
    }

    public int getTagCoding() {
        return tagCoding;
    }

    public void setTagCoding(int tagCoding) {
        this.tagCoding = tagCoding;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public boolean isReadByTurn() {
        return isReadByTurn;
    }

    public void setReadByTurn(boolean readByTurn) {
        isReadByTurn = readByTurn;
    }

   /* public long getReadInterval() {
        return readInterval;
    }

    public void setReadInterval(long readInterval) {
        this.readInterval = readInterval;
    }*/

    public List<Long> getReadIntervalList() {
        return readIntervalList;
    }

    public void setReadIntervalList(List<Long> readIntervalList) {
        this.readIntervalList = readIntervalList;
    }

    public static void setInstance(AntennaConfig instance) {
        AntennaConfig.instance = instance;
    }

    public int getSendDataModel() {
        return sendDataModel;
    }

    public void setSendDataModel(int sendDataModel) {
        this.sendDataModel = sendDataModel;
    }

    public long getSendDataInterval() {
        return sendDataInterval;
    }

    public void setSendDataInterval(long sendDataInterval) {
        this.sendDataInterval = sendDataInterval;
    }

    public boolean isAlternateTargetA_B() {
        return isAlternateTargetA_B;
    }

    public void setAlternateTargetA_B(boolean alternateTargetA_B) {
        isAlternateTargetA_B = alternateTargetA_B;
    }

    public long getTargetAlternateInterval() {
        return targetAlternateInterval;
    }

    public void setTargetAlternateInterval(long targetAlternateInterval) {
        this.targetAlternateInterval = targetAlternateInterval;
    }

    @Override
    public String toString() {
        return "ReaderConfig{" +
                "antennaList=" + Arrays.toString(antennaList) +
                ", readPower=" + readPower +
                ", baudRate=" + baudRate +
                ", blf=" + blf +
                ", tari=" + tari +
                ", tagCoding=" + tagCoding +
                ", target=" + target +
                ", session=" + session +
                ", isReadByTurn=" + isReadByTurn +
                ", readIntervalList=" + readIntervalList +
                ", sendDataModel=" + sendDataModel +
                ", sendDataInterval=" + sendDataInterval +
                ", isAlternateTargetA_B=" + isAlternateTargetA_B +
                ", targetAlternateInterval=" + targetAlternateInterval +
                '}';
    }
}

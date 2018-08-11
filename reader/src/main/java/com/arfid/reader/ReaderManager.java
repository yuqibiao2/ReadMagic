package com.arfid.reader;

import com.thingmagic.Gen2;
import com.thingmagic.ReadListener;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.SerialTransportAndroid;
import com.thingmagic.SerialTransportTCP;
import com.thingmagic.SimpleReadPlan;
import com.thingmagic.TMConstants;
import com.thingmagic.TagProtocol;
import com.thingmagic.TagReadData;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/8
 */
public class ReaderManager {

    private ReaderManager() {

    }

    private static class InstanceHolder {
        public static ReaderManager INSTANCE = new ReaderManager();
    }

    public static ReaderManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private Reader reader;

    public void contect(String query) {
        connect(query, new ReaderConfig());
    }

    /**
     * 连接读写器
     *
     * @param query
     * @param readerConfig
     */
    public Reader connect(String query, ReaderConfig readerConfig) {
        try {
            Reader.setSerialTransport("fuwit", new SerialTransportAndroid.Factory());
            Reader.setSerialTransport("tcp", new SerialTransportTCP.Factory());
            reader = Reader.create(query);
            reader.connect();
            initReaderParam(reader, readerConfig);
            return reader;
        } catch (Exception ex) {
            if (mOnReaderListener != null) {
                mOnReaderListener.onFailed(ex);
            }
            return null;
        }
    }

    /**
     * 开始读取
     */
    public void startReading() {
        if (reader == null) {
            throw new UnsupportedOperationException("请先初始化Reader，或初始化Reader失败");
        } else {
            reader.startReading();
            reader.addReadListener(new ReadListener() {
                @Override
                public void tagRead(Reader r, TagReadData t) {
                    if (mOnReaderListener != null) {
                        mOnReaderListener.onReadEpc(t.epcString());
                    }
                }
            });
        }
    }

    /**
     * 结束读取
     */
    public void stopReader() {
        reader.stopReading();
    }

    /**
     * 设置天线参数
     *
     * @param r
     * @throws ReaderException
     */
    private void initReaderParam(Reader r, ReaderConfig readerConfig) throws Exception {
        System.out.println("天线参数1===" + readerConfig);
        //---设置频点（NA：美标）
        String readerModel = (String) reader.paramGet("/reader/version/model");
        if (readerModel.equalsIgnoreCase("M6e Micro")) {
            reader.paramSet("/reader/region/id", Reader.Region.NA);
            int power = 3000;
            reader.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, power);
        } else if (readerModel.equalsIgnoreCase("M6e Nano")) {
            reader.paramSet("/reader/region/id", Reader.Region.NA2);
            int power = 2700;
            reader.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, power);
        } else {
            reader.paramSet("/reader/region/id", Reader.Region.NA);
            int power = 3000;
            reader.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, power);
        }
        //---设置天线
        SimpleReadPlan plan = new SimpleReadPlan(readerConfig.getAntennaList(), TagProtocol.GEN2, null, null, 1000);
        r.paramSet(TMConstants.TMR_PARAM_READ_PLAN, plan);
        //---设置功率
        r.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, readerConfig.getReadPower());
        //--设置波特率
        // r.paramSet(TMConstants.TMR_PARAM_BAUDRATE, readerConfig.getBaudRate());
        //--设置blf
        Gen2.LinkFrequency blf = readerConfig.getBlf();
        r.paramSet(TMConstants.TMR_PARAM_GEN2_BLF, blf);

        //--设置tari
        Gen2.Tari tari = readerConfig.getTari();
        r.paramSet(TMConstants.TMR_PARAM_GEN2_TARI, tari);

        //--设置tagCoding
        Gen2.TagEncoding tagenCoding = readerConfig.getTagenCoding();
        r.paramSet(TMConstants.TMR_PARAM_GEN2_TAGENCODING, tagenCoding);

        //--设置target
        Gen2.Target target = readerConfig.getTarget();
        //--设置Session
        Gen2.Session session = readerConfig.getSession();
        r.paramSet(TMConstants.TMR_PARAM_GEN2_SESSION, session);

    }


    private OnReaderListener mOnReaderListener;

    public void setmOnReaderListener(OnReaderListener mOnReaderListener) {
        this.mOnReaderListener = mOnReaderListener;
    }

    public interface OnReaderListener {
        void onReadEpc(String epc);

        void onFailed(Exception ex);
    }


}

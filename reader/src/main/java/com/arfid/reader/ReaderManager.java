package com.arfid.reader;

import android.os.Handler;
import android.os.Message;

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

    private ReaderConfig readerConfig = new ReaderConfig();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String epcStr = (String) msg.obj;
                    mOnReaderListener.onReadEpc(epcStr);
                    break;
            }
        }
    };

    private ReaderManager() {

    }

    private static class InstanceHolder {
        public static ReaderManager INSTANCE = new ReaderManager();
    }

    public static ReaderManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private Reader reader;

    private boolean isConnected = false;

    /**
     * 读写器是否已经连接
     *
     * @return
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * 读写器是否已经开始读取
     */
    private boolean isReaderStarted = false;

    public boolean isReaderStarted() {

        return isReaderStarted;
    }

    public Reader connect(String query) {

        return connect(query, readerConfig);
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
            setReaderParam(reader, readerConfig);
            if (mOnReaderListener != null) {
                mOnReaderListener.onSuccess();
            }
            isConnected = true;
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
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = t.epcString();
                        mHandler.sendMessage(msg);
                        //mOnReaderListener.onReadEpc(t.epcString());
                    }
                }
            });
        }
        isReaderStarted = true;
    }

    /**
     * 结束读取
     */
    public void stopReader() {
        reader.stopReading();
        isReaderStarted = false;
    }

    /**
     * 关闭单个Reader相关资源
     */
    public void closeReader() {
        if (reader != null) {
            reader.stopReading();
            reader.destroy();
        }
        isConnected = false;
    }


    /**
     * 设置天线参数
     *
     * @param r
     * @throws ReaderException
     */
    public void setReaderParam(Reader r, ReaderConfig readerConfig) throws Exception {
        this.readerConfig = readerConfig;
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
        Gen2.TagEncoding tagenCoding = readerConfig.getTagEncoding();
        r.paramSet(TMConstants.TMR_PARAM_GEN2_TAGENCODING, tagenCoding);

        //--设置target
        Gen2.Target target = readerConfig.getTarget();
        //--设置Session
        Gen2.Session session = readerConfig.getSession();
        r.paramSet(TMConstants.TMR_PARAM_GEN2_SESSION, session);

    }

    public ReaderConfig getReaderConfig() {
        return readerConfig;
    }

    private OnReaderListener mOnReaderListener;

    public void setmOnReaderListener(OnReaderListener mOnReaderListener) {
        this.mOnReaderListener = mOnReaderListener;
    }

    public interface OnReaderListener {
        void onReadEpc(String epc);

        void onSuccess();

        void onFailed(Exception ex);
    }


}

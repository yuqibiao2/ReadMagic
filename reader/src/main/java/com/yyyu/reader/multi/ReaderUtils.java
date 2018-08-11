package com.yyyu.reader.multi;

import android.util.Log;

import com.thingmagic.Gen2;
import com.thingmagic.ReadExceptionListener;
import com.thingmagic.ReadListener;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.SimpleReadPlan;
import com.thingmagic.TMConstants;
import com.thingmagic.TagProtocol;

import java.util.ArrayList;
import java.util.List;


/**
 * Reader操作相关工具类
 *
 * @author yu
 */
public class ReaderUtils {

    private static final String TAG = "ReaderUtils";

    private static List<ReaderAdapter> readerAdapterHolder;
    private boolean isAlternateTargetA_b;

    private static class InstanceHolder {
        private static ReaderUtils instance = new ReaderUtils();
    }

    private ReaderUtils() {

    }

    public static ReaderUtils getInstance() {
        readerAdapterHolder = new ArrayList<>();
        return InstanceHolder.instance;
    }


    /**
     * 初始化Reader
     */
    public ReaderAdapter initReader(String readerURI, AntennaConfig antennaConfig,
                                    ReadListener readListener, ReadExceptionListener readExceptionListener) {

        System.out.println("connect=============");

        ReaderAdapter readerAdapter = new ReaderAdapter();

        Reader r;
        // Create Reader object, connecting to physical device
        try {
            r = Reader.create(readerURI); // COMX parameter
            System.out.println("reader======" + r);
            r.connect();
            //---初始化天线参数
            initReaderParam(r, antennaConfig);
            //---设置读取监听
            r.addReadListener(readListener);
            //---设置读取异常监听
            r.addReadExceptionListener(readExceptionListener);
            readerAdapter.setReader(r);
            readerAdapter.setReadListener(readListener);
            readerAdapter.setReadExceptionListener(readExceptionListener);
            //---设置 target 切换相关属性值
            isAlternateTargetA_b = antennaConfig.isAlternateTargetA_B();
            if (isAlternateTargetA_b && antennaConfig.getSession()==2){//切换target
                readerAdapter.setAlternateTarget(antennaConfig.isAlternateTargetA_B());
                readerAdapter.setAlternateInterval(antennaConfig.getTargetAlternateInterval());
                readerAdapter.setAlternateFlag(true);
            }

            //---开启
            //r.startReading();
            readerAdapterHolder.add(readerAdapter);

        } catch (Exception re) {
            System.out.println("Reader 异常=== " + re.getMessage());
            closeReader(readerAdapter);
        }

        return readerAdapter;
    }

    /**
     * 设置天线参数
     *
     * @param r
     * @throws ReaderException
     */
    private void initReaderParam(Reader r, AntennaConfig antennaConfig) throws Exception {
        System.out.println("天线参数1===" + antennaConfig);
        //---设置频点（NA：美标）
        r.paramSet("/reader/region/id", Reader.Region.NA);
        //---设置天线
        SimpleReadPlan plan = new SimpleReadPlan(antennaConfig.getAntennaList(), TagProtocol.GEN2, null, null, 1000);
        r.paramSet(TMConstants.TMR_PARAM_READ_PLAN, plan);
        //---设置功率
        r.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, antennaConfig.getReadPower());
        //--设置波特率
        // r.paramSet(TMConstants.TMR_PARAM_BAUDRATE, antennaConfig.getBaudRate());
        //--设置blf
        Gen2.LinkFrequency blf = Gen2.LinkFrequency.LINK320KHZ;
        switch (antennaConfig.getBlf()) {
            case 0://LINK250KHZ
                blf = Gen2.LinkFrequency.LINK250KHZ;
                break;
            case 1://LINK320KHZ
                blf = Gen2.LinkFrequency.LINK320KHZ;
                break;
            case 2://LINK640KHZ
                blf = Gen2.LinkFrequency.LINK640KHZ;
                break;
        }
        r.paramSet(TMConstants.TMR_PARAM_GEN2_BLF, blf);

        //--设置tari
        Gen2.Tari tari = Gen2.Tari.TARI_6_25US;
        switch (antennaConfig.getTari()) {
            case 0://TARI_6_25US
                tari = Gen2.Tari.TARI_6_25US;
                break;
            case 1://TARI_12_5US
                tari = Gen2.Tari.TARI_12_5US;
                break;
            case 2://TARI_25US
                tari = Gen2.Tari.TARI_25US;
                break;
        }
        r.paramSet(TMConstants.TMR_PARAM_GEN2_TARI, tari);

        //--设置tagCoding
        Gen2.TagEncoding tagenCoding = Gen2.TagEncoding.FM0;
        switch (antennaConfig.getTagCoding()) {
            case 0://FM0
                tagenCoding = Gen2.TagEncoding.FM0;
                break;
            case 1://M2
                tagenCoding = Gen2.TagEncoding.M2;
                break;
            case 2://M4
                tagenCoding = Gen2.TagEncoding.M4;
                break;
            case 3://M8
                tagenCoding = Gen2.TagEncoding.M8;
                break;
        }
        r.paramSet(TMConstants.TMR_PARAM_GEN2_TAGENCODING, tagenCoding);

        //--设置target
        Gen2.Target target = Gen2.Target.A;
        switch (antennaConfig.getTarget()) {
            case 0://AB
                target = Gen2.Target.AB;
                break;
            case 1://BA
                target = Gen2.Target.BA;
                break;
            case 2://A
                target = Gen2.Target.A;
                break;
            case 3://B
                target = Gen2.Target.B;
                break;
        }
        if (antennaConfig.isAlternateTargetA_B()){//为切换target模式的时候不设置
            r.paramSet(TMConstants.TMR_PARAM_GEN2_TARGET, target);
        }
        //--设置Session
        Gen2.Session session = Gen2.Session.S1;
        switch (antennaConfig.getSession()) {
            case 0://s0
                session = Gen2.Session.S0;
                break;
            case 1://s1
                session = Gen2.Session.S1;
                break;
            case 2://s2
                session = Gen2.Session.S2;
                break;
            case 3://s3
                session = Gen2.Session.S3;
                break;
        }
        r.paramSet(TMConstants.TMR_PARAM_GEN2_SESSION, session);

    }

    /**
     * 关闭单个Reader相关资源
     *
     * @param readerAdapter
     */
    public void closeReader(ReaderAdapter readerAdapter) {
        Reader r = readerAdapter.getReader();
        if (r != null) {
            System.out.println("closeReader=============" + r);
            r.stopReading();
            r.removeReadListener(readerAdapter.getReadListener());
            r.removeReadExceptionListener(readerAdapter.getReadExceptionListener());
            r.destroy();
        }

    }

    /**
     * 关闭所有Reader相关资源
     */
    public void closeReader() {
        for (ReaderAdapter readerAdapter : readerAdapterHolder) {
            closeReader(readerAdapter);
        }
        readerAdapterHolder.clear();
        Constant.tagBuffer.clear();
    }

    /**
     * 停止所有reader
     */
    public void stopReader() {
        for (ReaderAdapter readerAdapter : readerAdapterHolder) {
            stopReader(readerAdapter);
        }
    }

    /**
     * 停止reader
     *
     * @param readerAdapter
     */
    private void stopReader(ReaderAdapter readerAdapter) {
        readerAdapter.setReaderStop(true);
        Reader reader = readerAdapter.getReader();
        //停止 target切换
        if (readerAdapter.isAlternateFlag()){
            readerAdapter.setAlternateFlag(false);
        }
        reader.stopReading();
    }

    /**
     * 多个读写器轮流工作
     */
    private int index = 0;
    private boolean isReaderByTuns = false;//是否交替工作
    public void startReaderByTurns(){
        if ( !AntennaConfig.getInstance().isReadByTurn()//不交替工作
                ||readerAdapterHolder.size()==1){//
            isReaderByTuns = false;
            startReader();
            return ;
        }
        isReaderByTuns = true;
        final int maxIndex = readerAdapterHolder.size()-1;
        final List<Long> readIntervalList = AntennaConfig.getInstance().getReadIntervalList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Constant.isStop) {
                        startReaderAndStopOthers(index);
                        long interval;
                        if (index < readIntervalList.size()) {
                            interval = readIntervalList.get(index);
                        } else {
                            interval = 0;
                        }
                        Thread.sleep(interval);
                        index++;
                        if (index > maxIndex) {
                            index = 0;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 开启一个读写器并关闭其它读写器
     * @param excludeIndex 0 开始
     */
    public void startReaderAndStopOthers(int excludeIndex) throws ReaderException {
        for (int i = 0; i < readerAdapterHolder.size(); i++) {
            ReaderAdapter readerAdapter = readerAdapterHolder.get(i);
            if (i==excludeIndex){
                if (readerAdapter.isAlternateTarget()){ //判断是否切换target
                    //int targetType = readerAdapter.getTargetType();
                    Object obj = readerAdapter.getReader().paramGet(TMConstants.TMR_PARAM_GEN2_TARGET);
                    int targetType = Utils.paseTargetParam(obj.toString());
                    //AB 轮流切换
                    if (targetType==2){//A-->B
                        readerAdapter.getReader().paramSet(TMConstants.TMR_PARAM_GEN2_TARGET,  Gen2.Target.B);
                        Log.e(TAG , "=======A---->B---1-- " + readerAdapter.getReader().toString());
                        readerAdapter.setTargetType(3);
                    }else if(targetType==3){//B-->A
                        readerAdapter.getReader().paramSet(TMConstants.TMR_PARAM_GEN2_TARGET,  Gen2.Target.A);
                        Log.e(TAG , "=======B--->A-----  "+readerAdapter.getReader().toString());
                        readerAdapter.setTargetType(2);
                    }
                }
                startReader(readerAdapter);
            }else{
                stopReader(readerAdapter);
            }
        }
    }

    /**
     * 开始所有reader
     */
    public void startReader() {
        for (ReaderAdapter readerAdapter : readerAdapterHolder) {
            startReader(readerAdapter);
        }
    }

    /**
     * 开始reader
     *
     * @param readerAdapter
     */
    private void startReader(ReaderAdapter readerAdapter) {
        readerAdapter.setReaderStop(false);
        Reader reader = readerAdapter.getReader();
        reader.startReading();
        if (isAlternateTargetA_b && !isReaderByTuns){//交替工作和不交替工作处理target切换方式不同
            readerAdapter.setAlternateFlag(true);
            alternateTargetA_B(readerAdapter);
        }
    }

    /**
     * 判断是否有读写器运行
     *
     * @return
     */
    public boolean isReaderHolderEmpty() {
        return readerAdapterHolder.size() <= 0;
    }


    /**
     * 交替替换 target A和B
     *
     * @param readerAdapter
     */
    public void alternateTargetA_B(final ReaderAdapter readerAdapter){
        final Reader  reader = readerAdapter.getReader();
        final long alternateInterval = readerAdapter.getAlternateInterval();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isA = true;
                while (readerAdapter.isAlternateFlag()){
                    try {
                        /*Object obj = readerAdapter.getReader().paramGet(TMConstants.TMR_PARAM_GEN2_TARGET);
                        int targetType = Utils.paseTargetParam(obj.toString());*/
                        if (!readerAdapter.isReaderStop()){
                            if (isA){//A
                                reader.stopReading();
                                reader.paramSet(TMConstants.TMR_PARAM_GEN2_TARGET,  Gen2.Target.B);
                                reader.startReading();
                                Log.e(  TAG , "=======A---->B---  "+readerAdapter.getReader().toString());
                                isA = false;
                            }else {//B
                                reader.stopReading();
                                reader.paramSet(TMConstants.TMR_PARAM_GEN2_TARGET,  Gen2.Target.A);
                                reader.startReading();
                                Log.e(  TAG , "=======B---->A--- "+readerAdapter.getReader().toString());
                                isA = true;
                            }
                            Thread.sleep(alternateInterval);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}

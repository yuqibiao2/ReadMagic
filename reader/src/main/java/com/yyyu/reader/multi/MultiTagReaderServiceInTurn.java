package com.yyyu.reader.multi;

import android.util.Log;

import com.thingmagic.ReadExceptionListener;
import com.thingmagic.ReadListener;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.TagReadData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static com.yyyu.reader.multi.Constant.PORT;
import static com.yyyu.reader.multi.Constant.buffCapacity;
import static com.yyyu.reader.multi.Constant.tagBuffer;

/**
 * 功能：读取天线数据通过Socket转发出去
 *
 * @author yu
 */
public class MultiTagReaderServiceInTurn {

    private OnSendTagDataListener mOnSendTagDataListener;

    private boolean isStopSend = false;
    private AntennaConfig antennaConfig;

    public static void main(String args[]) {
        new MultiTagReaderServiceInTurn().initSocket();
    }

    private void initSocket() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            //读取天线配置
            antennaConfig = AntennaPropertyOprUtils.getAntennaConfig("./cfg/antenna_config.property");
            while (true) {
                Socket client = serverSocket.accept();
                new HandleReaderThread(client).start();
                new HandleWriterThread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ---------------socket写数据
     */
    private class HandleWriterThread extends Thread {

        Socket mClient;
        StringBuffer sbEPC = new StringBuffer();

        public HandleWriterThread(Socket client) {
            this.mClient = client;
        }

        @Override
        public void run() {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(mClient.getOutputStream()));
                final BufferedWriter finalBw = bw;
                mOnSendTagDataListener = new OnSendTagDataListener() {
                    @Override
                    public void send(String data) {
                        if (mClient == null || finalBw == null) {
                            return;
                        }
                        sbEPC.append("@");
                        sbEPC.append(data);
                        sbEPC.append("#");
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSend = true;
                       while (isSend){
                           try {
                               if (mClient==null){
                                   ReaderUtils.getInstance().closeReader();
                                   isSend = false;
                                   continue;
                               }
                               Thread.sleep(antennaConfig.getSendDataInterval());
                               if (sbEPC!=null&&!"".equals(sbEPC.toString())){
                                   finalBw.write(sbEPC.toString());
                                   //LogUtils.log("=======上传数据==========="+sbEPC.toString());
                                   sbEPC = new StringBuffer();
                               }
                           } catch (Exception e) {
                               e.printStackTrace();
                           } finally {
                               try {
                                   finalBw.flush();
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * ------------------socket读数据
     */
    private class HandleReaderThread extends Thread {

        Socket mClient;

        private ReaderUtils readerUtils;

        public HandleReaderThread(Socket client) {
            this.mClient = client;
            readerUtils = ReaderUtils.getInstance();
        }

        @Override
        public void run() {
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
                while (mClient!=null) {
                    String readStr = br.readLine();
                    if (mClient == null || readStr == null) {
                        if (br != null) {
                            br.close();
                        }
                        return;
                    }
                    //System.out.println("readStr=====" + readStr);

                    /**
                     * 格式0端口号#天线开启状态&端口号#天线开启状态p读功率
                     * 03#0101&6#1010p3150  (p最大值为31.5*100)
                     */
                    if (readStr != null && readStr.startsWith("0") && readStr.length() >= 2) {//开始
                        String filterStr = readStr.substring(1, readStr.length());
                        //---分割出通用参数
                        String str1[] = filterStr.split("p");
                        String[] coms = str1[0].split("&");
                        if (!readerUtils.isReaderHolderEmpty()) {
                            readerUtils.closeReader();
                        }
                        for (String com : coms) {
                            String[] strings = com.split("#");
                            System.out.println("s");
                            String readerUri = "tmr:///COM" + strings[0];
                            System.out.println("readerUri：" + readerUri);
                            //ReaderConfig antennaConfig = new ReaderConfig();
                            int[] antennaList = Utils.toAntennaParams(strings[1]);
                            antennaConfig.setAntennaList(antennaList);
                            if (str1.length > 1) {
                                int readPower = Integer.parseInt(str1[1]);
                                antennaConfig.setReadPower(readPower);
                            }
                            ReaderAdapter readerAdapter = readerUtils.initReader(
                                    readerUri,
                                    antennaConfig,
                                    new ReadListener() {//---读取数据回调
                                        @Override
                                        public void tagRead(Reader reader, TagReadData tagReadData) {
                                            String epcStr = tagReadData.getTag().epcString();
                                            //System.out.println("读取-----" + epcStr);
                                            if (isStopSend) {//暂停状态
                                                return;
                                            }

                                            int sendDataModel = antennaConfig.getSendDataModel();

                                            if (mOnSendTagDataListener != null) {
                                                if (sendDataModel == 0) {//不过滤重复
                                                    mOnSendTagDataListener.send(epcStr);
                                                } else if (sendDataModel == 1) {//过滤重复
                                                    boolean isAdd = tagBuffer.add(epcStr);
                                                    if (tagBuffer.size() > buffCapacity) {
                                                        tagBuffer.clear();
                                                    }
                                                    if (isAdd) {
                                                        mOnSendTagDataListener.send(epcStr);
                                                        Log.e(TAG , "===读取==================size=========="+ tagBuffer.size());
                                                        //System.out.println("发送读取信息==send==" + epcStr + " tagBuffer.size()===" + tagBuffer.size() + "------");
                                                    }
                                                }
                                            }

                                        }
                                    },
                                    new ReadExceptionListener() {//---读取数据异常回掉
                                        String strDateFormat = "M/d/yyyy h:m:s a";
                                        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

                                        public void tagReadException(Reader r, ReaderException re) {
                                            String format = sdf.format(Calendar.getInstance().getTime());
                                            System.out.println("Reader Exception: " + re.getMessage() + " Occured on :" + format);
                                            readerUtils.closeReader();
                                            if (re.getMessage().equals("Connection Lost")) {
                                                System.exit(1);
                                            }
                                        }
                                    });
                        }
                        readerUtils.startReaderByTurns();
                    } else if ("1".equals(readStr)) {//暂停
                        if (!Constant.isStop ){
                            Log.e(TAG , "=====================暂停");
                            Constant.isStop = true;
                            readerUtils.stopReader();
                        }else{
                            Log.e(TAG , "===================还未开始无需暂停");
                        }
                    } else if ("2".equals(readStr)) {//重新开始
                        if(Constant.isStop){
                            Constant.isStop = false;
                            readerUtils.startReaderByTurns();
                            Log.e(TAG , "=====================重新开始");
                        }else{
                            Log.e(TAG , "=====================正在读取数据，不必重新开启");
                        }
                    } else if ("3".equals(readStr)) {//退出
                        readerUtils.closeReader();
                        System.exit(0);
                    } else if ("4".equals(readStr)) {//停止下发数据
                        isStopSend = true;
                    } else if ("5".equals(readStr)) {//重新下发数据
                        isStopSend = false;
                    } else if ("6".equals(readStr)) {
                        readerUtils.closeReader();
                    }else if("7".equals(readStr)){//清除数据，重新读、重新发
                        tagBuffer.clear();
                        Log.e(TAG , "tagBuffer.size()====="+tagBuffer.size());
                    }else{

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 通过Socket发送数据得回调
     */
    public interface OnSendTagDataListener {
        void send(String data);
    }


}

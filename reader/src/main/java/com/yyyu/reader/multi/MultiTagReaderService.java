package com.yyyu.reader.multi;

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

import static com.yyyu.reader.multi.Constant.PORT;
import static com.yyyu.reader.multi.Constant.buffCapacity;
import static com.yyyu.reader.multi.Constant.tagBuffer;

/**
 * 功能：读取天线数据通过Socket转发出去
 *
 * @author yu
 */
public class MultiTagReaderService {

    private OnSendTagDataListener mOnSendTagDataListener;

    private boolean isStopSend = false;

    public static void main(String args[]) {
        new MultiTagReaderService().initSocket();
    }

    private void initSocket() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket client = serverSocket.accept();
                new HandleReaderThread(client).start();
                new HandleWriterThread(client).start();
                System.out.println("initSocket===============");
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
                        try {
                            finalBw.write(data);
                            //System.out.println("发送读取信息==send==" + data + "size===" + tagBuffer.size());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                finalBw.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
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
                while (true) {
                    String readStr = br.readLine();
                    if (mClient == null || readStr == null) {
                        if (br != null) {
                            br.close();
                        }
                        return;
                    }
                    System.out.println("readStr=====" + readStr);

                    //读取天线配置
                    AntennaConfig antennaConfig = AntennaPropertyOprUtils.getAntennaConfig("./cfg/antenna_config.property");

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
                            if (str1.length>1){
                                int readPower = Integer.parseInt(str1[1]);
                                antennaConfig.setReadPower(readPower);
                            }
                            readerUtils.initReader(
                                    readerUri,
                                    antennaConfig,
                                    new ReadListener() {//---读取数据回调
                                        @Override
                                        public void tagRead(Reader reader, TagReadData tagReadData) {
                                            String epcStr = tagReadData.getTag().epcString();
                                            System.out.println("读取-----"+epcStr);
                                            if (isStopSend) {//暂停状态
                                                return;
                                            }
                                            boolean isAdd = tagBuffer.add(epcStr);
                                            if (tagBuffer.size() > buffCapacity) {
                                                tagBuffer.clear();
                                            }
                                            if (isAdd && mOnSendTagDataListener != null) {
                                                mOnSendTagDataListener.send(epcStr);
                                                System.out.println("发送读取信息==send==" + epcStr + "size===" + tagBuffer.size() + "------");
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

                    } else if ("1".equals(readStr)) {//暂停
                        readerUtils.stopReader();
                    } else if ("2".equals(readStr)) {//重新开始
                       readerUtils.startReader();
                    } else if ("3".equals(readStr)) {//退出
                        readerUtils.closeReader();
                        System.exit(0);
                    }else if ("4".equals(readStr)) {//停止下发数据
                        isStopSend = true;
                    } else if ("5".equals(readStr)) {//重新下发数据
                        isStopSend = false;
                    }else if("6".equals(readStr)){
                        readerUtils.closeReader();
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

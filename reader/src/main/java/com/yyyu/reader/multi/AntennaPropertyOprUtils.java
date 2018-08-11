package com.yyyu.reader.multi;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 功能：property文件的读写工具类
 *
 * @author yu
 * @date 2017/7/25.
 */
public class AntennaPropertyOprUtils {


    public static void main(String[] args) {
        Map<String, String> result = parseProperty2Map("./cfg/antenna_config.property");
        AntennaConfig antennaConfig = getAntennaConfig("./cfg/antenna_config.property");
        System.out.println("antennaConfig===" + antennaConfig);
    }

    /**
     * 将读取的配置文件封装成AntennaConfig对象
     *
     * @param filePath
     * @return
     */
    public static AntennaConfig getAntennaConfig(String filePath) {
        AntennaConfig antennaConfig = AntennaConfig.getInstance();
        Map<String, String> paramsMap = parseProperty2Map(filePath);
        for (Map.Entry<String, String> paramsEntry : paramsMap.entrySet()) {
            String key = paramsEntry.getKey();
            String value = paramsEntry.getValue();
            if ("readPower".equals(key)) {
                antennaConfig.setReadPower(Integer.parseInt(value));
            } else if ("baudRate".equals(key)) {
                antennaConfig.setBaudRate(Integer.parseInt(value));
            } else if ("blf".equals(key)) {
                antennaConfig.setBlf(Integer.parseInt(value));
            } else if ("tari".equals(key)) {
                antennaConfig.setTari(Integer.parseInt(value));
            } else if ("tagCoding".equals(key)) {
                antennaConfig.setTagCoding(Integer.parseInt(value));
            } else if ("target".equals(key)) {
                antennaConfig.setTarget(Integer.parseInt(value));
            } else if ("session".equals(key)) {
                antennaConfig.setSession(Integer.parseInt(value));
            }else if("is_read_by_turn".equals(key)){
                int readByTurn = Integer.parseInt(value);
                if (readByTurn==0){
                    antennaConfig.setReadByTurn(false);
                }else{
                    antennaConfig.setReadByTurn(true);
                }
            }
            else if ("read_interval".equals(key)){
                String[] strs = value.split("&");
                List<Long> readIntervalList = new ArrayList<>();
                for (int i = 0; i <strs.length ; i++) {
                    readIntervalList.add(Long.parseLong(strs[i]));
                }
               antennaConfig.setReadIntervalList(readIntervalList);
            }else if("send_data_model".equals(key)){
                antennaConfig.setSendDataModel(Integer.parseInt(value));
            }else if ("send_data_interval".equals(key)){
                antennaConfig.setSendDataInterval(Long.parseLong(value));
            }else if("is_alternate_target_A_B".equals(key)){
                int isAlternateTargetA_B = Integer.parseInt(value);
                if (isAlternateTargetA_B==0){
                    antennaConfig.setAlternateTargetA_B(false);
                }else if (isAlternateTargetA_B==1){
                    antennaConfig.setAlternateTargetA_B(true);
                }
            }else if("alternate_target_A_B_interval".equals(key)){
                antennaConfig.setTargetAlternateInterval(Long.parseLong(value));
            }
        }

        return antennaConfig;
    }

    /**
     * 读取property文件存在map集合中
     *
     * @param filePath
     * @return
     */
    public static Map<String, String> parseProperty2Map(String filePath) {

        Map<String, String> result = new HashMap();

        Properties prop = new Properties();
        try {
            //读取属性文件
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            prop.load(in);     ///加载属性列表
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = prop.getProperty(key);
                result.put(key, value);
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }

}

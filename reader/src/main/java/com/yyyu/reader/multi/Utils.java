package com.yyyu.reader.multi;


import java.util.ArrayList;
import java.util.List;

/**
 *工具方法
 *
 * @author yu
 *
 */
public class Utils {

    /**
     * 将0101这样的字符串转换位天线的配置
     *
     *分别对应着四个天线0表示关闭，1表示开启
     *
     * @param str
     * @return
     */
    public static int[] toAntennaParams(String str){

        List<Integer> antennaList = new ArrayList<>();

        char[] chars = str.toCharArray();
        if (chars[0]=='1'){
            antennaList.add(1);
        }
        if (chars[1]=='1'){
            antennaList.add(2);
        }
        if (chars[2]=='1'){
            antennaList.add(3);
        }
        if (chars[3]=='1'){
            antennaList.add(4);
        }

        return toIntArray(antennaList.toArray(new Integer[antennaList.size()]));

    }

    private static int[] toIntArray(Integer[] integers){
        int [] ints = new int[integers.length];
        for (int i=0 ; i<integers.length ; i++){
            ints[i] = integers[i];
        }
        return ints;
    }

    public static int  paseTargetParam(String param){
        if ("A".equals(param)){
            return 2;
        }else if("B".equals(param)){
            return 3;
        }else if("AB".equals(param)){
            return 0;
        }else if("BA".equals(param)){
            return 1;
        }else{
            return -1;
        }
    }

}

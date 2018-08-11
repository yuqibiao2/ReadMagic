package com.yyyu.reader.multi;

import java.util.HashSet;
import java.util.Set;

/**
 * 全局变量(配置信息)
 *
 * @author yu
 */
public class Constant {

    /**
     * 监听的端口号
     */
    public static final int PORT = 50090;
    /**
     * 用于过滤重复得标签
     */
    public static Set<String> tagBuffer = new HashSet<>();
    /**
     * 过滤重复set集合最大容量
     */
    public static final int buffCapacity = Integer.MAX_VALUE;

    public static final long READ_INTERVAL = 5*1000;

    public static  boolean isStop = false;

}

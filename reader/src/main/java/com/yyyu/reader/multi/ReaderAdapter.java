package com.yyyu.reader.multi;

import com.thingmagic.ReadExceptionListener;
import com.thingmagic.ReadListener;
import com.thingmagic.Reader;

/**
 * Reader适配类
 * 实现Reader、ReadListener、TagReadExceptionReceiver、之间得绑定
 *
 * @author yu
 */
class ReaderAdapter {

    private Reader reader;
    private ReadListener readListener;
    private ReadExceptionListener readExceptionListener;
    private boolean isAlternateTarget;//是否切换target
    private boolean alternateFlag;//切换线程flag
    private long alternateInterval = 1000;
    private boolean isReaderStop = false;
    private int targetType;

    public ReaderAdapter() {

    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public ReadListener getReadListener() {
        return readListener;
    }

    public void setReadListener(ReadListener readListener) {
        this.readListener = readListener;
    }

    public ReadExceptionListener getReadExceptionListener() {
        return readExceptionListener;
    }

    public void setReadExceptionListener(ReadExceptionListener readExceptionListener) {
        this.readExceptionListener = readExceptionListener;
    }

    public boolean isAlternateTarget() {
        return isAlternateTarget;
    }

    public void setAlternateTarget(boolean alternateTarget) {
        isAlternateTarget = alternateTarget;
    }

    public boolean isAlternateFlag() {
        return alternateFlag;
    }

    public void setAlternateFlag(boolean alternateFlag) {
        this.alternateFlag = alternateFlag;
    }

    public long getAlternateInterval() {
        return alternateInterval;
    }

    public void setAlternateInterval(long alternateInterval) {
        this.alternateInterval = alternateInterval;
    }

    public boolean isReaderStop() {
        return isReaderStop;
    }

    public void setReaderStop(boolean readerStop) {
        isReaderStop = readerStop;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }
}
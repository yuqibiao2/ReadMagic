package com.arfid.readmagic;

import android.app.Application;

import com.thingmagic.Reader;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/6
 */
public class MyApplication extends Application{

    private Reader reader;

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}

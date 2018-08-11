/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package org.winplus.serial.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.util.Log;
/*
 * SerialPort????JNI???????????????????
 */
public class SerialPort {

	private static final String TAG = "SerialPort";
	
	
	public static int TNCOM_EVENPARITY = 0;//?У??
	public static int TNCOM_ODDPARITY = 1 ;//??У??

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;
	private boolean trig_on=false;
	byte[] test;
	public SerialPort(String path, int baudrate) throws SecurityException, IOException {
		File device= new File(path);
		if (!device.canRead() || !device.canWrite()) {
			try {
				String command = "chmod 777 " + device.getAbsolutePath();
				Runtime runtime = Runtime.getRuntime();

				Process proc = runtime.exec(command);
			} catch (IOException e) {
				//Log.i("chw", "chmod fail!!  !!");
				e.printStackTrace();
				throw new SecurityException();
			}
			//Log.i("chw", "Get serial port read/write   permission");
		}
		
		mFd = open(path, baudrate, 0);
		
		if (mFd == null) {
			//Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		
		
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
		
	}

	// Getters and setters
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}

//	/**
//	 * ???????У??
//	 * @param mode  
//	 */
//	public void setPortparity(int mode){
//		setPortParity(mode);
//	}
	
	// JNI
	
	//private native static FileDescriptor open(String path, int baudrate);
	public native void close();
	
//	public native void setPortParity(int mode); //????У??λ
	
	public native void test(byte[] bytes);
	
	// JNI
	private native static FileDescriptor open(String path, int baudrate,int flags);

	
	static {
		System.out.println("load so#####################");
		System.loadLibrary("serial_port");
	}
	
}

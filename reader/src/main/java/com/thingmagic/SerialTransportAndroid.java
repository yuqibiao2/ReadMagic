package com.thingmagic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.winplus.serial.utils.SerialPort;

//import android.serialport.SerialPort;


public class SerialTransportAndroid implements SerialTransport
{
	
	/** serialport **/
	private SerialPort mSerialPort ;
	private InputStream is ;
	private OutputStream os ;
	
	private String path ;
	private int baudrate  = 115200;

	public SerialTransportAndroid(String readerUri) {
		// TODO Auto-generated constructor stub
		path = readerUri;
	}

	public void open() throws ReaderException {
		// TODO Auto-generated method stub
		//open
		try {
			mSerialPort = new SerialPort(path, baudrate);
		}catch (Exception e) {
			
			//Toast.makeText(this, "SerialPort init fail!!", 0).show();
			return;
		}
		is = mSerialPort.getInputStream();
		os = mSerialPort.getOutputStream();
	}

	public void sendBytes(int length, byte[] message, int offset, int timeoutMs)
			throws ReaderException {
		// TODO Auto-generated method stub
	       try
	        {
	            if(os == null)
	            {
	               throw new ReaderException("serial Connection lost");
	            }
	            System.out.println("sendBytes : " + message[2]);

	            os.write(message, offset, length);
	        }
	        catch(Exception ex)
	        {
	          throw new ReaderCommException(ex.getMessage());
	        }
	}

	public byte[] receiveBytes(int length, byte[] messageSpace, int offset,
			int timeoutMillis) throws ReaderException {
		// TODO Auto-generated method stub
	       try
	        {
	            if(is == null)
	            {
	               throw new IOException("TCP Connection lost");
	            }
	            int responseWaitTime = 0;
	            while (is.available() < length && responseWaitTime <timeoutMillis)
	            {
	                Thread.sleep(10);
	                // Repeat the loop for every 10 milli sec untill we receive required
	                // data
	                responseWaitTime+=10;
	            }
	            if (is.available() <= 0)
	            {
	                throw new IOException("Timeout");
	            }
	            is.read(messageSpace, offset, length);
				System.out.println("receiveBytes : %x" + messageSpace[2]);
	        }
	        catch(Exception ex)
	        {
	          throw new ReaderCommException(ex.getMessage());
	        }
	        return messageSpace;
	}

	public int getBaudRate() throws ReaderException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setBaudRate(int rate) throws ReaderException {
		// TODO Auto-generated method stub
		
	}

	public void flush() throws ReaderException {
		// TODO Auto-generated method stub
		
	}

	public void shutdown() throws ReaderException {
		// TODO Auto-generated method stub
		mSerialPort.close();
	}
	
	  static public class Factory implements ReaderFactory
	  { 

	      public SerialReader createReader(String uriString) throws ReaderException
	      {
	          String readerUri = null;
	          try 
	          {
	              URI uri = new URI(uriString);
	              readerUri = uri.getPath();
	          } 
	          catch (Exception ex) 
	          {
	              
	          }
	          return new SerialReader(readerUri, new SerialTransportAndroid(readerUri));
	      }
	    }
}

package fuwit.com.ffreader;

import android.app.Application;

import com.thingmagic.*;
/**
 * Created by DELL on 2018/4/29.
 */

public class MyApplication extends Application {
    public Reader reader= null;

    boolean open_device(String query) {
        try {
            Reader.setSerialTransport("fuwit", new SerialTransportAndroid.Factory());
            Reader.setSerialTransport("tcp", new SerialTransportTCP.Factory());
            System.out.println("device path:"+ query);
            reader = Reader.create(query);
            reader.connect();
            int[] antennaList = {1};
            System.out.println("connect success");
            SimpleReadPlan plan = new SimpleReadPlan(antennaList, TagProtocol.GEN2, null, null, 1000);
            reader.paramSet(TMConstants.TMR_PARAM_READ_PLAN, plan);

            String readerModel=(String)reader.paramGet("/reader/version/model");
            if(readerModel.equalsIgnoreCase("M6e Micro")){
                reader.paramSet("/reader/region/id", Reader.Region.NA);
                int power = 3000;
                reader.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, power);
            }else if(readerModel.equalsIgnoreCase("M6e Nano")){
                reader.paramSet("/reader/region/id", Reader.Region.NA2);
                int power = 2700;
                reader.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, power);
            } else {
                reader.paramSet("/reader/region/id", Reader.Region.NA);
                int power = 3000;
                reader.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, power);
            }

            Gen2.Session session = Gen2.Session.S1;
            reader.paramSet(TMConstants.TMR_PARAM_GEN2_SESSION, session);

        } catch (ReaderException e) {
            System.out.println("error e:"+ e.getMessage());
            return false;
            //e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("error ex:"+ ex.getMessage());
            return false;
        }
        return true;
    }

    void close_device()
    {
        if(reader != null) {
            reader.destroy();
        }
    }

}

package fuwit.com.ffreader;

import android.app.Activity;
import android.content.Intent;
import android.extra.emsh.EmshConstant;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.thingmagic.*;
/**
 * Created by DELL on 2018/4/29.
 */

public class myreader extends Activity{
    public static final String RETURN_INFO = "fuwit.com.ffreader";

    private TextView tvTagCount;
    private ListView lvEpc;
    private Button btnStart ;
    private Button btnClear ;
    private Button btConnect;
    private EditText etURL;
    private TextView tvTime;
    private Button btSetting;

    private Set<String> epcSet = null ; //store different EPC
    private List<EPC> listEpc = null;//EPC list
    private Map<String, Integer> mapEpc = null; //store EPC position

    private int allCount = 0 ;
    private EPCadapter adapter ;

    private long lastTime =  0L ;

    private Reader reader = null;
    private ReadListener rl;
    private Timer timer;
    String device = null;

    private int isStop = 0;

    private MyApplication mapp ;
    //HTimerTask timer;

    /*public  Reader getReader()
    {
        return reader;
    }*/

    private void enableUartComm_UHF( boolean bEnable ) {
        Intent intent = new Intent( EmshConstant.Action.INTENT_EMSH_REQUEST );
        intent.putExtra( EmshConstant.IntentExtra.EXTRA_COMMAND, EmshConstant.Command.CMD_REQUEST_ENABLE_UHF_COMM );
        intent.putExtra( EmshConstant.IntentExtra.EXTRA_PARAM_1, (bEnable ? 1 : 0 ) );

        sendBroadcast(intent);
    }

    private void setPowerState_UHF( boolean bPowerOn ) {
        Intent intent = new Intent( EmshConstant.Action.INTENT_EMSH_REQUEST );
        intent.putExtra( EmshConstant.IntentExtra.EXTRA_COMMAND, EmshConstant.Command.CMD_REQUEST_SET_POWER_MODE );
        intent.putExtra( EmshConstant.IntentExtra.EXTRA_PARAM_1,
                (bPowerOn ? EmshConstant.EmshBatteryPowerMode.EMSH_PWR_MODE_DSG_UHF : EmshConstant.EmshBatteryPowerMode.EMSH_PWR_MODE_STANDBY) );
        sendBroadcast(intent);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String epc = msg.getData().getString("epc");
                    if (epc == null || epc.length() == 0) {
                        return ;
                    }
                    int position ;
                    allCount++ ;
//                    epc += allCount ;
                    if (epcSet == null) {//first add
                        epcSet = new HashSet<String>();
                        listEpc = new ArrayList<EPC>();
                        mapEpc = new HashMap<String, Integer>();
                        epcSet.add(epc);
                        mapEpc.put(epc, 0);
                        EPC epcTag = new EPC() ;
                        epcTag.epc = epc ;
                        epcTag.count = 1 ;
                        listEpc.add(epcTag);
                        adapter = new EPCadapter(myreader.this, listEpc);
                        lvEpc.setAdapter(adapter);
                        //Util.play(1, 0);
                        //mapp.setEpcSet(epcSet);
                    }else{
                        if (epcSet.contains(epc)) {//set already exit
                            position = mapEpc.get(epc);
                            EPC epcOld = listEpc.get(position);
                            epcOld.count += 1 ;
                            listEpc.set(position, epcOld);
                        }else{
                            epcSet.add(epc);
                            mapEpc.put(epc, listEpc.size());
                            EPC epcTag = new EPC() ;
                            epcTag.epc = epc ;
                            epcTag.count = 1 ;
                            listEpc.add(epcTag);
                            //mapp.setEpcSet(epcSet);
                        }

                        if(System.currentTimeMillis() - lastTime > 100){
                            lastTime = System.currentTimeMillis() ;
                            //Util.play(1, 0);
                        }
                        //tvTagCount.setText(listEpc.size());
                        tvTagCount.setText("" + listEpc.size());
                        adapter.notifyDataSetChanged();


//                        lvEpc.setSelection(listEpc.size() - 1);

                    }
                    break ;
                case 2:
                    int time = Integer.parseInt(tvTime.getText().toString()) + 100;
                    String t = ""+time;
                    tvTime.setText(t);
                    break;
            }
        }
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial);

        this.findAllViewsById();
        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());*/

        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectAll()
                .penaltyLog().build());*/

        btConnect.setOnClickListener(readerconnect);
        btnStart.setOnClickListener(startRead);
        btnClear.setOnClickListener(clearRead);
        //etURL.setEnabled(false);
        btSetting.setOnClickListener(setParm);
        getINFO();
        mapp = (MyApplication) getApplication() ;
    }

    private void getINFO() {
        // 获取传递过来的信息。
        device = getIntent().getStringExtra(RETURN_INFO);

    }


    private void findAllViewsById()
    {
        //System.out.println("findAllViewsById");
        btConnect = (Button) findViewById(R.id.btConnect);
        btnStart = (Button) findViewById(R.id.button_start);
        btnClear = (Button) findViewById(R.id.button_clear_epc);
        etURL = (EditText) findViewById(R.id.etURL);
        lvEpc = (ListView) findViewById(R.id.listView_epc);
        tvTagCount = (TextView) findViewById(R.id.textView_tag);
        tvTime = (TextView) findViewById(R.id.tvTime);
        btSetting = (Button) findViewById(R.id.btSetting);

    }

    private View.OnClickListener setParm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(reader == null) {
                toastShow("未连接设备");
                return;
            }
            Intent intent = new Intent(myreader.this, ReaderSetting.class);
            startActivity(intent);

        }
    };


    private View.OnClickListener readerconnect = new View.OnClickListener() {
        public void onClick(View v) {
            if(btConnect.getText().equals("Connect")) {
                String query  = null;
                String str = etURL.getText().toString();

                //query ="fuwit:///dev/ttyMT3";
                //query = "fuwit:///dev/ttyS2";
                System.out.println("device :"+ device);

                //try{
                    if(device.equals("serial")) {
                        //Reader.setSerialTransport("fuwit", new SerialTransportAndroid.Factory());
                        query = "fuwit://" + str;
                    }  else {
                        //Reader.setSerialTransport("tcp", new SerialTransportTCP.Factory());
                        query = "tcp://" + str + ":8086";
                    }
                    if(mapp.open_device(query)) {
                        toastShow("连接成功");
                        btConnect.setText("Disconnect");
                        //etURL.setVisibility(8);
                        etURL.setEnabled(false);
                        reader = mapp.reader;
                    } else {
                        toastShow("连接失败");
                        return;
                    }
                //}

                    /*reader = Reader.create(query);
                    reader.connect();
                    int[] antennaList = {1};

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
                    }

                    Gen2.Session session = Gen2.Session.S1;
                    reader.paramSet(TMConstants.TMR_PARAM_GEN2_SESSION, session);

                } catch (ReaderException e) {
                    // TODO Auto-generated catch block
                    toastShow("连接失败");
                    return;
                    //e.printStackTrace();
                }
*/

            } else {
                if(reader != null) {
                    reader.destroy();
                    reader = null;
                    btConnect.setText("Connect");
                    etURL.setEnabled(true);
                    toastShow("断开连接");
                }
            }
        }
    };

    private void toastShow(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener startRead = new View.OnClickListener() {
        public void onClick(View v) {
            if(reader == null) {
                toastShow("未连接设备");
                return;
            }

            if(btnStart.getText().equals("盘点")) {
                // Create and add tag listener
                timer = new Timer();

                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isStop == 1)
                            return;
                        TagReadData[] tagReads;
                        while(true) {
                            try {
                                tagReads = reader.read(500);
                                // Print tag reads
                                System.out.println("count :" + tagReads.length);
                                for (TagReadData tr : tagReads)
                                {

                                    Message msg = new Message() ;
                                    msg.what = 1 ;
                                    Bundle b = new Bundle();
                                    b.putString("epc", tr.epcString());
                                    msg.setData(b);
                                    handler.sendMessage(msg);
                                }
                            } catch (ReaderException ex) {
                                System.out.println("Reader Exception : " + ex.getMessage());
                            }
                        }
                        //reader.startReading();
                    }
                }).start();*/

                rl = new PrintListener();
                reader.addReadListener(rl);

               /* new Thread(new Runnable() {
                    @Override
                    public void run() {
                        reader.startReading();
                    }
                }).start();*/
                reader.startReading();
                //timer = new HTimerTask();
                btConnect.setEnabled(false);
                btSetting.setEnabled(false);
                btnStart.setText("停止盘点");
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                }, 100, 100);
            } else {
              /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        reader.stopReading();

                    }
                }).start();*/
                //isStop = 1;
                // ֹͣ��ʱ��
                reader.stopReading();

                /*try {
                    Thread.sleep(500);
                }catch (Exception ex) {

                }*/

                stopTimer();

                reader.removeReadListener(rl);
                btnStart.setText("盘点");
                btConnect.setEnabled(true);
                btSetting.setEnabled(true);
            }
        }
    };

    private View.OnClickListener clearRead = new View.OnClickListener() {
        public void onClick(View v) {
            if (epcSet != null) {
                epcSet.removeAll(epcSet); //store different EPC
            }
            if(listEpc != null)
                listEpc.removeAll(listEpc);//EPC list
            if(mapEpc != null)
                mapEpc.clear(); //store EPC position
            if(adapter != null)
                adapter.notifyDataSetChanged();
            allCount = 0 ;
            tvTagCount.setText("0");
            tvTime.setText("0");
        }
    };

    class PrintListener implements ReadListener
    {
        public void tagRead(Reader r, TagReadData tr)
        {
            //System.out.println("Background read: " + tr.toString());
            Message msg = new Message() ;
            msg.what = 1 ;
            Bundle b = new Bundle();
            b.putString("epc", tr.epcString());
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    public void stopTimer(){
        if(timer != null){
            timer.cancel();
            // һ������Ϊnull������ʱ�����ᱻ����
            timer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(device.equals("serial")) {
            //Power.on(10);
            //enableUartComm_UHF(true);
            //setPowerState_UHF(true);
        }
    }

    @Override
    protected void onDestroy() {
        if(reader != null) {
            reader.destroy();
            reader = null;
        }
        if(device.equals("serial")) {
            //Power.off(10);
            //setPowerState_UHF(false);
            //enableUartComm_UHF(false);
        }
         System.out.println("close myreader");
        super.onDestroy();
    }

}

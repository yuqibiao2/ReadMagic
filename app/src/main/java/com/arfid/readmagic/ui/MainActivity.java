package com.arfid.readmagic.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arfid.reader.ReaderManager;
import com.arfid.readmagic.MyApplication;
import com.arfid.readmagic.R;
import com.arfid.readmagic.adapter.EPCShowAdapter;
import com.arfid.readmagic.bean.EPCBean;
import com.thingmagic.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private EditText etPort;
    private Button btnConnect;
    private Button btnStart;
    private Button btnClear;
    private Button btnSetting;
    private ReaderManager readerManager;
    private MyApplication application;
    private ListView lvEpc;
    private HashMap<String, Integer> ecpContainer;
    private List<EPCBean> epcList;
    private EPCShowAdapter epcShowAdapter;
    private TextView tvTotal;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        ecpContainer = new HashMap<>();
        epcList = new ArrayList<>();
        application = (MyApplication) getApplication();
        //1.得到readerManager实例
        readerManager = ReaderManager.getInstance();
    }

    @Override
    protected void initView() {
        etPort = findViewById(R.id.et_port);
        btnConnect = findViewById(R.id.btn_connect);
        btnStart = findViewById(R.id.btn_start);
        btnClear = findViewById(R.id.btn_clear);
        btnSetting = findViewById(R.id.btn_setting);
        lvEpc = findViewById(R.id.lv_epc);
        tvTotal = findViewById(R.id.tv_total);

        epcShowAdapter = new EPCShowAdapter(this, epcList);
        lvEpc.setAdapter(epcShowAdapter);
    }

    @Override
    protected void initListener() {

        //---连接读写器
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (readerManager.isConnected()) {//取消连接
                    showLoadingDialog("正在断开连接...");
                    readerManager.closeReader();
                    btnConnect.setText("连接");
                    btnStart.setText("开始");
                    btnStart.setEnabled(false);
                    btnSetting.setEnabled(false);
                } else {
                    showLoadingDialog("正在连接中....");
                    String str = etPort.getText().toString();
                    String query = "fuwit://" + str;
                    if (TextUtils.isEmpty(query)) {
                        Toast.makeText(MainActivity.this, "串口地址不能为空", Toast.LENGTH_LONG);
                        return;
                    }
                    //2.通过串口地址连接设备
                    Reader reader = readerManager.connect(query);
                    application.setReader(reader);
                    btnStart.setText("开始");
                    btnStart.setEnabled(true);
                }
            }
        });

        //---开始读取
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!readerManager.isReaderStarted()) {//读写器为开始读取
                    //3.开启读取数据
                    readerManager.startReading();
                    btnStart.setText("结束");
                    btnSetting.setEnabled(false);
                    showLoadingDialog("正在开启...");
                } else {
                    showLoadingDialog("正在结束...");
                    readerManager.stopReader();
                    btnStart.setText("开始");
                    btnSetting.setEnabled(true);
                }
            }
        });

        //---读写器回调
        readerManager.setmOnReaderListener(new ReaderManager.OnReaderListener() {
            @Override
            public void onReadEpc(String epc) {
                Log.e(TAG, "onReadEpc: ===" + epc);
                if (!ecpContainer.keySet().contains(epc)) {
                    ecpContainer.put(epc, epcList.size());
                    EPCBean ecp = new EPCBean(epc, 1);
                    epcList.add(ecp);
                } else {
                    Integer position = ecpContainer.get(epc);
                    EPCBean epcBean = epcList.get(position);
                    epcBean.setCount(epcBean.getCount() + 1);
                }
                tvTotal.setText("" + epcList.size());
                epcShowAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(application, "读写器已连接", Toast.LENGTH_SHORT).show();
                btnStart.setEnabled(true);
                btnSetting.setEnabled(true);
                btnConnect.setText("取消连接");
            }

            @Override
            public void onFailed(Exception ex) {
                Toast.makeText(application, "读写器异常：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //---清除
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ecpContainer.clear();
                epcList.clear();
                epcShowAdapter.notifyDataSetChanged();
                tvTotal.setText("0");
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.startAction(MainActivity.this);
            }
        });

    }


}

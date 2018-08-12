package com.arfid.readmagic.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.arfid.reader.ReaderConfig;
import com.arfid.reader.ReaderManager;
import com.arfid.readmagic.MyApplication;
import com.arfid.readmagic.R;
import com.thingmagic.Gen2;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity {

    private static final String TAG = "SettingActivity";

    private RadioGroup rgBlf;
    private RadioGroup rgTari;
    private RadioGroup rgTag;
    private RadioGroup rgTarget;
    private RadioGroup rgSession;
    private SeekBar sbPower;
    private CheckBox cbAnt1;
    private CheckBox cbAnt2;
    private CheckBox cbAnt3;
    private CheckBox cbAnt4;

    private boolean isAnt1Checked = true;
    private boolean isAnt2Checked = true;
    private boolean isAnt3Checked = true;
    private boolean isAnt4Checked = true;
    private Button btnSure;
    private ReaderConfig readerConfig;
    private MyApplication application;
    private ReaderManager readerManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void beforeInit() {
        application = (MyApplication) getApplication();
        readerManager = ReaderManager.getInstance();
        readerConfig = readerManager.getReaderConfig();
    }

    @Override
    protected void initView() {

        cbAnt1 = findViewById(R.id.cb_ant1);
        cbAnt2 = findViewById(R.id.cb_ant2);
        cbAnt3 = findViewById(R.id.cb_ant3);
        cbAnt4 = findViewById(R.id.cb_ant4);

        rgBlf = findViewById(R.id.rg_blf);
        rgTari = findViewById(R.id.rg_tari);
        rgTag = findViewById(R.id.rg_tag);
        rgTarget = findViewById(R.id.rg_target);
        rgSession = findViewById(R.id.rg_session);
        sbPower = findViewById(R.id.sb_power);

        RadioButton rb_250 = findViewById(R.id.rb_250);
        RadioButton rb_320 = findViewById(R.id.rb_320);
        RadioButton rb_640 = findViewById(R.id.rb_640);
        RadioButton rb_25 = findViewById(R.id.rb_25);
        RadioButton rb_12_5 = findViewById(R.id.rb_12_5);
        RadioButton rb_6_25 = findViewById(R.id.rb_6_25);
        RadioButton rb_fm0 = findViewById(R.id.rb_fm0);
        RadioButton rb_m2 = findViewById(R.id.rb_m2);
        RadioButton rb_m4 = findViewById(R.id.rb_m4);
        RadioButton rb_m8 = findViewById(R.id.rb_m8);
        RadioButton rb_ab = findViewById(R.id.rb_ab);
        RadioButton rb_ba = findViewById(R.id.rb_ba);
        RadioButton rb_a = findViewById(R.id.rb_a);
        RadioButton rb_b = findViewById(R.id.rb_b);
        RadioButton rb_s0 = findViewById(R.id.rb_s0);
        RadioButton rb_s1 = findViewById(R.id.rb_s1);
        RadioButton rb_s2 = findViewById(R.id.rb_s2);
        RadioButton rb_s3 = findViewById(R.id.rb_s3);

        btnSure = findViewById(R.id.btn_sure);

        ReaderConfig readerConfig = ReaderManager.getInstance().getReaderConfig();

        int[] antennaList = readerConfig.getAntennaList();
        cbAnt1.setChecked(false);
        cbAnt2.setChecked(false);
        cbAnt3.setChecked(false);
        cbAnt4.setChecked(false);
        for (int ant : antennaList) {
            if (ant == 1) {
                cbAnt1.setChecked(true);
            } else if (ant == 2) {
                cbAnt2.setChecked(true);
            } else if (ant == 3) {
                cbAnt3.setChecked(true);
            } else if (ant == 4) {
                cbAnt4.setChecked(true);
            }
        }
        int readPower = readerConfig.getReadPower();
        sbPower.setProgress(readPower);
        Gen2.LinkFrequency blf = readerConfig.getBlf();
        switch (blf) {
            case LINK250KHZ:
                rb_250.setChecked(true);
                break;
            case LINK320KHZ:
                rb_320.setChecked(true);
                break;
            case LINK640KHZ:
                rb_640.setChecked(true);
                break;
        }
        Gen2.TagEncoding tagEncoding = readerConfig.getTagEncoding();
        switch (tagEncoding) {
            case FM0:
                rb_fm0.setChecked(true);
                break;
            case M2:
                rb_m2.setChecked(true);
                break;
            case M4:
                rb_m4.setChecked(true);
                break;
            case M8:
                rb_m8.setChecked(true);
                break;
        }
        Gen2.Tari tari = readerConfig.getTari();
        switch (tari) {
            case TARI_25US:
                rb_25.setChecked(true);
                break;
            case TARI_6_25US:
                rb_6_25.setChecked(true);
                break;
            case TARI_12_5US:
                rb_12_5.setChecked(true);
                break;
        }
        Gen2.Target target = readerConfig.getTarget();
        switch (target) {
            case A:
                rb_a.setChecked(true);
                break;
            case B:
                rb_b.setChecked(true);
                break;
            case AB:
                rb_ab.setChecked(true);
                break;
            case BA:
                rb_ba.setChecked(true);
                break;
        }
        Gen2.Session session = readerConfig.getSession();
        switch (session) {
            case S0:
                rb_s0.setChecked(true);
                break;
            case S1:
                rb_s1.setChecked(true);
                break;
            case S2:
                rb_s2.setChecked(true);
                break;
            case S3:
                rb_s3.setChecked(true);
                break;
        }

    }

    @Override
    protected void initListener() {

        cbAnt1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAnt1Checked = b;
            }
        });
        cbAnt2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAnt2Checked = b;
            }
        });
        cbAnt3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAnt3Checked = b;
            }
        });
        cbAnt4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAnt4Checked = b;
            }
        });

        rgBlf.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_250:
                        readerConfig.setBlf(Gen2.LinkFrequency.LINK250KHZ);
                        break;
                    case R.id.rb_320:
                        readerConfig.setBlf(Gen2.LinkFrequency.LINK320KHZ);
                        break;
                    case R.id.rb_640:
                        readerConfig.setBlf(Gen2.LinkFrequency.LINK640KHZ);
                        break;
                }
            }
        });

        rgTari.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_25:
                        readerConfig.setTari(Gen2.Tari.TARI_25US);
                        break;
                    case R.id.rb_12_5:
                        readerConfig.setTari(Gen2.Tari.TARI_12_5US);
                        break;
                    case R.id.rb_6_25:
                        readerConfig.setTari(Gen2.Tari.TARI_6_25US);
                        break;
                }
            }
        });

        rgTag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_fm0:
                        readerConfig.setTagEncoding(Gen2.TagEncoding.FM0);
                        break;
                    case R.id.rb_m2:
                        readerConfig.setTagEncoding(Gen2.TagEncoding.M2);
                        break;
                    case R.id.rb_m4:
                        readerConfig.setTagEncoding(Gen2.TagEncoding.M4);
                        break;
                    case R.id.rb_m8:
                        readerConfig.setTagEncoding(Gen2.TagEncoding.M8);
                        break;
                }
            }
        });

        rgTarget.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_ab:
                        readerConfig.setTarget(Gen2.Target.AB);
                        break;
                    case R.id.rb_ba:
                        readerConfig.setTarget(Gen2.Target.BA);
                        break;
                    case R.id.rb_a:
                        readerConfig.setTarget(Gen2.Target.A);
                        break;
                    case R.id.rb_b:
                        readerConfig.setTarget(Gen2.Target.B);
                        break;
                }
            }
        });

        rgSession.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_s0:
                        readerConfig.setSession(Gen2.Session.S0);
                        break;
                    case R.id.rb_s1:
                        readerConfig.setSession(Gen2.Session.S1);
                        break;
                    case R.id.rb_s2:
                        readerConfig.setSession(Gen2.Session.S2);
                        break;
                    case R.id.rb_s3:
                        readerConfig.setSession(Gen2.Session.S3);
                        break;
                }
            }
        });


        sbPower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                readerConfig.setReadPower(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //--确认设置
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Integer> antList = new ArrayList<>();
                if (isAnt1Checked) {
                    antList.add(1);
                }
                if (isAnt2Checked) {
                    antList.add(2);
                }
                if (isAnt3Checked) {
                    antList.add(3);
                }
                if (isAnt4Checked) {
                    antList.add(4);
                }
                int[] antIntList = toIntArray(antList.toArray(new Integer[antList.size()]));
                readerConfig.setAntennaList(antIntList);

                Log.e(TAG, "readerConfig: " + readerConfig);

                try {
                    readerManager.setReaderParam(application.getReader(), readerConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                finish();
            }
        });

    }

    private int[] toIntArray(Integer[] integers) {
        int[] ints = new int[integers.length];
        for (int i = 0; i < integers.length; i++) {
            ints[i] = integers[i];
        }
        return ints;
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

}

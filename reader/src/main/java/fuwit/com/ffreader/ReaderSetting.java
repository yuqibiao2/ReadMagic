package fuwit.com.ffreader;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.thingmagic.*;
/**
 * Created by DELL on 2018/4/29.
 */

public class ReaderSetting extends Activity{
    private EditText edtPower;
    private Reader reader;
    private MyApplication mmap;
    private Button btSetPower;
    private RadioGroup rgGen2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        mmap = (MyApplication) getApplication();
        btSetPower = (Button) findViewById(R.id.btnSetPower);
        rgGen2 = (RadioGroup) findViewById(R.id.rgGen2);
        edtPower = (EditText) findViewById(R.id.edtPower);
        reader = mmap.reader;

        btSetPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(btSetPower.getText())){
                    toastShow("不能为空");
                    return;
                }
                int power = Integer.parseInt(edtPower.getText().toString());
                try {
                    reader.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, power);
                } catch (ReaderException e) {
                    toastShow(e.getMessage());
                    return;
                }
                toastShow("设置成功");
            }
        });

        rgGen2.setOnCheckedChangeListener(listen);

        try {
            edtPower.setText(reader.paramGet(TMConstants.TMR_PARAM_RADIO_READPOWER).toString());
        } catch (ReaderException e) {
            toastShow(e.getMessage());
            return;
        }
    }

    private RadioGroup.OnCheckedChangeListener listen = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = group.getCheckedRadioButtonId();
            if (id == R.id.rb1) {
                set_gen2(Gen2.LinkFrequency.LINK250KHZ, Gen2.Tari.TARI_25US, Gen2.TagEncoding.M4, Gen2.Session.S0, Gen2.Target.AB);

            } else if (id == R.id.rb2) {
                set_gen2(Gen2.LinkFrequency.LINK250KHZ, Gen2.Tari.TARI_25US, Gen2.TagEncoding.M4, Gen2.Session.S1, Gen2.Target.A);

            } else if (id == R.id.rb3) {
                set_gen2(Gen2.LinkFrequency.LINK320KHZ, Gen2.Tari.TARI_6_25US, Gen2.TagEncoding.FM0, Gen2.Session.S1, Gen2.Target.A);

            }
        }
    };

    void set_gen2(Gen2.LinkFrequency blf, Gen2.Tari tari, Gen2.TagEncoding tage, Gen2.Session session, Gen2.Target target){
        try {
            reader.paramSet(TMConstants.TMR_PARAM_GEN2_BLF, blf);
            reader.paramSet(TMConstants.TMR_PARAM_GEN2_TARI, tari);
            reader.paramSet(TMConstants.TMR_PARAM_GEN2_SESSION, session);
            reader.paramSet(TMConstants.TMR_PARAM_GEN2_TAGENCODING, tage);
            reader.paramSet(TMConstants.TMR_PARAM_GEN2_TARGET, target);
        } catch (ReaderException e) {
            toastShow(e.getMessage());
            return;
        }
        toastShow("设置成功");
    }

    private void toastShow(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}

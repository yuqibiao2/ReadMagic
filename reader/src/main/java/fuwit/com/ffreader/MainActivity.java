package fuwit.com.ffreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private TextView tvSerial;
    private TextView tvNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSerial = (TextView) findViewById(R.id.tvSerial);
        tvSerial.setOnClickListener(this);
        tvNetwork = (TextView) findViewById(R.id.tvNetwork);
        tvNetwork.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), myreader.class);
        Bundle bundle = new Bundle();
        int i = view.getId();
        if (i == R.id.tvSerial) {
            bundle.putString(myreader.RETURN_INFO, "serial");

        } else if (i == R.id.tvNetwork) {
            bundle.putString(myreader.RETURN_INFO, "network");

        }

        intent.putExtras(bundle);
        startActivity(intent);
    }

}

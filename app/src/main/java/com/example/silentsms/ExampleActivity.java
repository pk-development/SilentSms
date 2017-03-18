package com.example.silentsms;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.HashMap;

import pk.development.sms.Constants;
import pk.development.sms.detection.Sniffer;
import pk.development.sms.model.SmsData;

public class ExampleActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        tv = (TextView)findViewById(R.id.tvTest);
        /*
            This is only an example and should be saved to
            a database.

            <unique hash, smsdata>

            The unique hash is so we don't keep alerting on
            the same logcat line if re-detected.
         */
        final Map<String, SmsData> testData = new HashMap<>();

        /*
            If a message is detected in the Sniffer Thread it is
            post here.
         */
        Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case Constants.SMS_DETECTED:
                        SmsData tmpData = (SmsData)msg.obj;

                        //Only add new SmsData Object if md5 has not stored
                        if(!testData.containsKey(tmpData.getSmsHash())) {
                            testData.put(tmpData.getSmsHash(), tmpData);
                        }
                        tv.setText(tmpData.toString() + "\n--------\nMessages Detected:" + testData.size());
                        break;
                }

            }
        };
        final Sniffer sniff = new Sniffer(mHandler, getApplicationContext());

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Sniffer.isSnifferRunning) {
                    Sniffer.isSnifferRunning = false;
                    tv.setText("Stopped\nList size: " + testData.size());
                } else {
                    Thread testT = new Thread(sniff);
                    Sniffer.isSnifferRunning = true;
                    testT.start();
                    tv.setText("Running...");
                }
           }
        });
    }
}
# SilentSms
Small project to detect silent sms from logcat on certain Android devices.

This is work in progress and not finished but if you find useful fork it and use in your project.

The pk.development.sms package has all the classes need to get working in your project.

Here is an example of what you need to start sms detection in your project, 
When the Sniffer thread detects an sms the it sends the SmsData Object to your main activity.

Once at a stable state and up to coding stardards hoping to implement to into AIMSICD to replace 
the old crappy model I 1st started of with that has to many issues with threading. 

Root is required on your device to allow app to read logcat radio buffer

```Java

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
        
```        

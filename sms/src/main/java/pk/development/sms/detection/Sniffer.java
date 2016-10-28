package pk.development.sms.detection;
/**
 <Silent Sms is an api that can be used to detect silent sms on an Android Devices>
 Copyright (C) <2016>  <Paul Kinsella>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pk.development.sms.Constants;
import pk.development.sms.enums.SmsType;
import pk.development.sms.model.BtsData;
import pk.development.sms.utils.SmsUtils;

public class Sniffer implements Runnable {
    private static final int LINE_RESET_BUFFER = 200;
    private static final String TAG = "Sniffer";
    private Handler mHandler;
    private CellSniffer cellSniffer;
    public static boolean isSnifferRunning;

    public Sniffer(Handler mHandler, Context context) {
        this.mHandler = mHandler;
        this.cellSniffer =  CellSniffer.getInstance(context);
    }

    @Override
    public void run() {
        BufferedReader lineReader = null;
        DataOutputStream dos = null;

        try {
            Process process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes("logcat -v time -b radio\n");
            dos.flush();

            lineReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            isSnifferRunning = true;
        } catch (IOException e) {
            Log.e(TAG, "Error at start " + e);

            return;
        } finally {
            if(dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        String line;
        List<String> lines = new ArrayList<>();

        do {

            try {

                if((line = lineReader.readLine()) != null && lines.size() < LINE_RESET_BUFFER ) {
                    lines.add(line);
                } else if(lines.size() == 0) {
                    //No lines yet so slow down thread
                    Thread.sleep(1500);
                } else {
                    for(String dataLine : lines) {

                        switch(SmsUtils.scanLine(dataLine)) {
                            case FlASH:
                                logDetectedSms(lines, dataLine, SmsType.FlASH);
                                break;
                            case TYPE0:
                                logDetectedSms(lines, dataLine, SmsType.TYPE0);
                                break;
                            case MWI:
                                logDetectedSms(lines, dataLine, SmsType.MWI);
                                break;
                            case WAPPUSH:
                                logDetectedSms(lines, dataLine, SmsType.WAPPUSH);
                                break;
                        }
                    }
                    // Lines where checked so clear List
                    lines.clear();
                }

            } catch (InterruptedException|IOException e) {
                Log.e(TAG, "Error in Sniffer " + e);
            }

        } while(isSnifferRunning);
    }

    /*   TODO:
     *   In a full working app a hash check to a database is needed rather
     *   than wasting resources creating an sms object that we already detected.
     */
    private void logDetectedSms(List<String> lines, String dataLine, SmsType smsType) {

        mHandler.obtainMessage(Constants.SMS_DETECTED,
                SmsUtils.createSmsObject(
                        lines,
                        dataLine,
                        new BtsData(cellSniffer.getBtsData()),
                        smsType)
        ).sendToTarget();
    }
}

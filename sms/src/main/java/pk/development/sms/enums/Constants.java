package pk.development.sms.enums;
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

import java.util.Hashtable;

import pk.development.sms.enums.enums.SmsType;

public class Constants {

    public final static int SMS_DETECTED = 0x1001;

    public final static Hashtable<String, SmsType> DETECTION_FLAGS;

    static {
        //TODO: in a fully working app DETECTION_FLAGS should be loaded from a database
        DETECTION_FLAGS = new Hashtable<>();

        DETECTION_FLAGS.put("Received short message type 0", SmsType.TYPE0);
        DETECTION_FLAGS.put("isTypeZero=true", pk.development.sms.enums.enums.SmsType.TYPE0);
        DETECTION_FLAGS.put("Received voice mail indicator clear SMS shouldStore=false", SmsType.MWI);
        DETECTION_FLAGS.put("SMS TP-PID:0 data coding scheme:24", SmsType.FlASH);
        //DETECTION_FLAGS.put("SMS TP-PID:0 data coding scheme:4", SmsType.WAPPUSH);
        DETECTION_FLAGS.put("incoming msg. Mti 0 ProtocolID 0 DCS 0x04 class -1", SmsType.WAPPUSH);
        //DETECTION_FLAGS.put("Unsupported SMS data coding scheme 4", SmsType.WAPPUSH);
    }
}

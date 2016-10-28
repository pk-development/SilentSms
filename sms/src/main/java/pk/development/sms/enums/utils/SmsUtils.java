package pk.development.sms.enums.utils;
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pk.development.sms.enums.Constants;
import pk.development.sms.enums.enums.SmsType;
import pk.development.sms.enums.model.BtsData;
import pk.development.sms.enums.model.SmsData;

public class SmsUtils {

    public static String extractMobileNumber(List<String> logcatLines) {

        if (logcatLines != null && logcatLines.size() > 0) {
            for (String line : logcatLines) {
                if (line.contains("SMS originating address:") && line.contains("+")) {
                    return line.substring(line.indexOf("+"));
                } else if (line.contains("OrigAddr")) {
                    return line.substring(line.indexOf("OrigAddr") + 8).trim();
                }
            }
        }

        return "Unknown";
    }

    public static String extractSmsText(List<String> logcatLines) {

        if (logcatLines != null && logcatLines.size() > 0) {
            for (String line : logcatLines) {
                if (line.contains("SMS message body (raw):") && line.contains("'")) {
                    return line.substring(line.indexOf("'") + 1,
                            line.length() - 1);
                }
            }
        }

        return "No Sms Data";
    }

    /**
     * Returns SmsType from detected logcat line
     * TYPE0, MWI, WAPPUSH, FLASH
     * @param line
     * @return
     */
    public static SmsType scanLine(String line) {
        Iterator it = Constants.DETECTION_FLAGS.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(line.contains((String)pair.getKey())) {
                return (SmsType)pair.getValue();
            }
        }
        return SmsType.NO_TYPE;
    }

    public static final Pattern LOGCAT_TIMESTAMP_PATTERN = Pattern.compile("^(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})");

    /**
     *  Creates an SmsData Object that contains all the details of
     *  the event
     * @see SmsData for fields
     * @param logcatLines
     * @param alertLine
     * @return
     */
    public static SmsData createSmsObject(
            List<String> logcatLines,
            String alertLine,
            BtsData btsData,
            SmsType smsType) {

        String md5Hash = null;
        Matcher m = LOGCAT_TIMESTAMP_PATTERN.matcher(alertLine);

        if(m.find()) {
            md5Hash = MD5(m.group().trim());
        }

        return new SmsData(
                extractMobileNumber(logcatLines),
                extractSmsText(logcatLines),
                System.currentTimeMillis(),
                smsType,
                btsData,
                md5Hash);
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}

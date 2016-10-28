package pk.development.sms.enums.model;
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

import pk.development.sms.enums.enums.SmsType;

public class SmsData {

    private String senderNumber;
    private String messageText;
    private long timestamp;
    private SmsType smsType;
    private BtsData btsData;
    private String smsHash; // A unique md5 hash

    public SmsData() {}

    public SmsData(String senderNumber, String messageText,
                   long timestamp, SmsType smsType,
                   BtsData btsData, String smsHash) {

        this.senderNumber = senderNumber;
        this.messageText = messageText;
        this.timestamp = timestamp;
        this.smsType = smsType;
        this.btsData = btsData;
        this.smsHash = smsHash;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public SmsType getSmsType() {
        return smsType;
    }

    public void setSmsType(SmsType smsType) {
        this.smsType = smsType;
    }

    public BtsData getBtsData() {
        return btsData;
    }

    public void setBtsData(BtsData btsData) {
        this.btsData = btsData;
    }

    public String getSmsHash() {
        return smsHash;
    }

    public void setSmsHash(String smsHash) {
        this.smsHash = smsHash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sender_number: " + senderNumber + "\n");
        sb.append("sms_data: " + messageText + "\n");
        sb.append("is_roaming: " + btsData.isRoaming() + "\n");
        sb.append("timestamp: " + timestamp + "\n");
        sb.append("sms_type: " + smsType + "\n");
        sb.append(btsData);

        return sb.toString();
    }
}

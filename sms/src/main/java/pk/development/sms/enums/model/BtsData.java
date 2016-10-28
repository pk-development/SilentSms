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

import pk.development.sms.enums.enums.BtsType;

public class BtsData {

    private int lac;
    private int cid;
    private int mnc;
    private int mcc;
    private int signalStrength;
    private boolean isRoaming;

    private GeoPoint geoPoint;
    private BtsType btsType;

    public BtsData(){}

    public BtsData(BtsData btsData){
        this.lac = btsData.getLac();
        this.cid = btsData.getCid();
        this.mnc = btsData.getMnc();
        this.mcc = btsData.getMcc();
        this.signalStrength = btsData.getSignalStrength();
        this.isRoaming = btsData.isRoaming();
        this.geoPoint = btsData.getGeoPoint();
        this.btsType = btsData.getBtsType();
    }

    public BtsData(int lac, int cid,
                   int mnc, int mcc,
                   int signalStrength, boolean isRoaming,
                   GeoPoint geoPoint, BtsType btsType) {

        this.lac = lac;
        this.cid = cid;
        this.mnc = mnc;
        this.mcc = mcc;
        this.signalStrength = signalStrength;
        this.isRoaming = isRoaming;
        this.geoPoint = geoPoint;
        this.btsType = btsType;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    public boolean isRoaming() {
        return isRoaming;
    }

    public void setRoaming(boolean roaming) {
        isRoaming = roaming;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public BtsType getBtsType() {
        return btsType;
    }

    public void setBtsType(BtsType btsType) {
        this.btsType = btsType;
    }

    @Override
    public String toString() {
        return  "cid:" + cid + "\n"+
                "lac:" + lac + "\n" +
                "signal:" + signalStrength + "\n" +
                "mcc: " + mcc + "\n" +
                "mnc: " + mnc + "\n" +
                "lat: " + geoPoint.getLatitude() + "\n" +
                "lon: " + geoPoint.getLongitude() + "\n";
    }
}

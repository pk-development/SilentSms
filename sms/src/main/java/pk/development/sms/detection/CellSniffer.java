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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import pk.development.sms.enums.BtsType;
import pk.development.sms.model.BtsData;
import pk.development.sms.model.GeoPoint;

/**
 * Created by Paul Kinsella on 27/10/2016.
 *
 * This Singleton class gathers Cell data and Gps data that the Sniffer class
 * accesses when it detects a silent sms.
 *
 * Ideally there should only be one instance of Sniffer accessing this class
 * to avoid threading issues.
 */
public class CellSniffer extends PhoneStateListener implements LocationListener {
    private static CellSniffer instance;
    private Context context;
    private TelephonyManager teleManager;
    private LocationManager locationManager;
    private BtsData btsData;

    private CellSniffer(Context context) {
        this.context = context;
        btsData = new BtsData();
        btsData.setGeoPoint(new GeoPoint(0, 0));

        teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        teleManager.listen(this, LISTEN_CELL_LOCATION | LISTEN_CELL_INFO | LISTEN_SIGNAL_STRENGTHS);
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    100, this);
    }

    public synchronized static CellSniffer getInstance(Context context) {
        return (instance == null)
                ? instance = new CellSniffer(context)
                : instance;
    }

    public BtsData getBtsData() {
        return btsData;
    }

    @Override
    public void onCellLocationChanged(CellLocation location) {

        switch (teleManager.getPhoneType()) {
            case TelephonyManager.PHONE_TYPE_GSM:
                GsmCellLocation gsmCellData = (GsmCellLocation) teleManager.getCellLocation();
                if (gsmCellData != null) {
                    btsData.setLac(gsmCellData.getLac());
                    btsData.setCid(gsmCellData.getCid());
                    btsData.setRoaming(teleManager.isNetworkRoaming());
                    btsData.setBtsType(BtsType.GSM);

                    String networkProvider = teleManager.getNetworkOperator();
                    if(!TextUtils.isEmpty(networkProvider)) {
                        btsData.setMcc(Integer.parseInt(networkProvider.substring(0, 3)));
                        btsData.setMnc(Integer.parseInt(networkProvider.substring(3)));
                    }
                }
                break;
            case TelephonyManager.PHONE_TYPE_CDMA:
                CdmaCellLocation cdmaCellData = (CdmaCellLocation) teleManager.getCellLocation();
                if (cdmaCellData != null) {
                    btsData.setLac(cdmaCellData.getNetworkId());
                    btsData.setCid(cdmaCellData.getBaseStationId());
                    btsData.setBtsType(BtsType.CDMA);
                }
                break;
        }
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
       btsData.setSignalStrength(signalStrength.getGsmSignalStrength());
    }

    @Override
    public void onLocationChanged(Location location) {
        btsData.setGeoPoint(new GeoPoint(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {    }

    @Override
    public void onProviderEnabled(String s) {    }

    @Override
    public void onProviderDisabled(String s) {    }
}

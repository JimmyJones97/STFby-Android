package  com.xzy.forestSystem.gogisapi.GPS;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import com.xzy.forestSystem.gogisapi.Common.Common;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LocalGPSDevice extends GPSDevice {
    private Iterator<GpsSatellite> Iteratorsate;
    private Iterable<GpsSatellite> allSatellites;
    private Context context = null;
    private GpsStatus gpsStatus;
    private LocationManager locationManager;
    private LocationListener myLocationListener = new LocationListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.LocalGPSDevice.3
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location == null || LocalGPSDevice.this.gpsLocation == null) {
                String msg = String.valueOf("") + "还没有定位！";
                return;
            }
            LocalGPSDevice.this.gpsLocation.UpdateLocation(location.getTime(), location.getLongitude(), location.getLatitude(), location.getAltitude(), (double) location.getAccuracy(), (double) (location.getSpeed() * 3.6f), location.getBearing());
            LocalGPSDevice.this.gpsLocation.status = "单点";
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String provider) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String provider) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private GpsStatus.NmeaListener nmeaListener = new GpsStatus.NmeaListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.LocalGPSDevice.1
        @Override // android.location.GpsStatus.NmeaListener
        public void onNmeaReceived(long arg0, String arg1) {
            if (arg0 > 0 && LocalGPSDevice.this.gpsLocation != null) {
                LocalGPSDevice.this.gpsLocation.updateRecData(arg1);
            }
        }
    };
    private GpsStatus.Listener statusListener = new GpsStatus.Listener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.LocalGPSDevice.2
        @Override // android.location.GpsStatus.Listener
        public void onGpsStatusChanged(int event) {
            List<SatelliteInfo> tempSatellites = new ArrayList<>();
            LocalGPSDevice.this.gpsStatus = LocalGPSDevice.this.locationManager.getGpsStatus(null);
            if (LocalGPSDevice.this.gpsStatus != null) {
                LocalGPSDevice.this.allSatellites = LocalGPSDevice.this.gpsStatus.getSatellites();
                LocalGPSDevice.this.Iteratorsate = LocalGPSDevice.this.allSatellites.iterator();
                int tempCount = 0;
                int tempCount2 = 0;
                while (LocalGPSDevice.this.Iteratorsate.hasNext()) {
                    GpsSatellite satellite = (GpsSatellite) LocalGPSDevice.this.Iteratorsate.next();
                    SatelliteInfo tempSate = new SatelliteInfo();
                    tempSate.f471ID = satellite.getPrn();
                    tempSate.Azimuth = satellite.getAzimuth();
                    tempSate.Elevation = satellite.getElevation();
                    tempSate.Snr = satellite.getSnr();
                    tempSatellites.add(tempSate);
                    tempCount++;
                    if (satellite.usedInFix()) {
                        tempCount2++;
                    }
                }
                if (LocalGPSDevice.this.gpsLocation != null) {
                    LocalGPSDevice.this.gpsLocation.satellitesCount = tempCount;
                    LocalGPSDevice.this.gpsLocation.usedSateCount = tempCount2;
                }
            }
            LocalGPSDevice.this.satellites = tempSatellites;
        }
    };

    public LocalGPSDevice(Context context2, GPSLocationClass gpsLocation) {
        super(gpsLocation);
        this.context = context2;
        this.deviceName = "内置GPS设备";
    }

    @Override //  com.xzy.forestSystem.gogisapi.GPS.GPSDevice
    public void startConnect() {
        super.startConnect();
        try {
            if (!Settings.Secure.isLocationProviderEnabled(this.context.getContentResolver(), "gps")) {
                Common.ShowToast("GPS设备没有开启,请先开启GPS设备.");
                return;
            }
            this.locationManager = (LocationManager) this.context.getSystemService("location");
            this.locationManager.addGpsStatusListener(this.statusListener);
            this.locationManager.addNmeaListener(this.nmeaListener);
            this.locationManager.requestLocationUpdates("gps", 0, 0.0f, this.myLocationListener);
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.GPS.GPSDevice
    public void StopGPSDevice() {
        if (!this.isDisposed) {
            super.StopGPSDevice();
            try {
                if (this.locationManager != null) {
                    this.locationManager.removeGpsStatusListener(this.statusListener);
                    this.locationManager.removeNmeaListener(this.nmeaListener);
                    this.locationManager.removeUpdates(this.myLocationListener);
                }
                if (this.myLocationListener != null) {
                    this.myLocationListener = null;
                }
                if (this.statusListener != null) {
                    this.statusListener = null;
                }
                if (this.nmeaListener != null) {
                    this.nmeaListener = null;
                }
            } catch (Exception e) {
            }
            this.isDisposed = true;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        if (this.locationManager != null) {
            this.locationManager.removeGpsStatusListener(this.statusListener);
            this.locationManager.removeUpdates(this.myLocationListener);
            this.locationManager = null;
        }
        if (this.myLocationListener != null) {
            this.myLocationListener = null;
        }
        if (this.statusListener != null) {
            this.statusListener = null;
        }
    }
}
